package org.masaa.reader.data
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
class PdfPageRenderer(private val resolver:ContentResolver,private val uri:Uri):AutoCloseable {
 private var descriptor:ParcelFileDescriptor?=null
 private var renderer:PdfRenderer?=null
 private val closed=AtomicBoolean(false)
 private suspend fun open():PdfRenderer=withContext(Dispatchers.IO) {
  check(!closed.get())
  renderer ?: run { val fd=resolver.openFileDescriptor(uri,"r") ?: error("Cannot open PDF"); descriptor=fd; PdfRenderer(fd).also { renderer=it } }
 }
 suspend fun pageCount()=open().pageCount
 suspend fun render(index:Int,targetWidth:Int):Bitmap=withContext(Dispatchers.IO) {
  val pdf=open(); require(index in 0 until pdf.pageCount)
  synchronized(pdf) { pdf.openPage(index).use { page ->
   val w=targetWidth.coerceIn(480,1800); val h=(w*page.height.toFloat()/page.width.coerceAtLeast(1)).toInt().coerceAtLeast(1)
   Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888).also { it.eraseColor(Color.WHITE); page.render(it,null,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY) }
  } }
 }
 override fun close() { if(closed.compareAndSet(false,true)) { runCatching { renderer?.close() }; runCatching { descriptor?.close() }; renderer=null; descriptor=null } }
}
