package org.masaa.reader.ui
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.masaa.reader.data.InteractiveLayerLoader
import org.masaa.reader.data.PdfPageRenderer
import org.masaa.reader.model.InteractiveElement
import org.masaa.reader.model.resolveExactHit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderHome() {
 val context=LocalContext.current
 var uriText by rememberSaveable { mutableStateOf<String?>(null) }
 var pageIndex by rememberSaveable { mutableIntStateOf(0) }
 var pageCount by rememberSaveable { mutableIntStateOf(0) }
 var bitmap by remember { mutableStateOf<Bitmap?>(null) }
 var error by remember { mutableStateOf<String?>(null) }
 var selected by remember { mutableStateOf<InteractiveElement?>(null) }
 var zoom by rememberSaveable { mutableFloatStateOf(1f) }
 var renderer by remember { mutableStateOf<PdfPageRenderer?>(null) }
 val loader=remember { InteractiveLayerLoader(context) }
 val picker=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri:Uri? ->
  if(uri!=null) {
   runCatching { context.contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION) }
   uriText=uri.toString(); pageIndex=0; selected=null; zoom=1f
  }
 }
 LaunchedEffect(uriText) {
  renderer?.close(); renderer=null; bitmap=null; pageCount=0; error=null
  val value=uriText ?: return@LaunchedEffect
  runCatching { PdfPageRenderer(context.contentResolver,Uri.parse(value)).also { renderer=it; pageCount=it.pageCount() } }
   .onFailure { error=it.message ?: "تعذّر فتح الملف" }
 }
 LaunchedEffect(renderer,pageIndex) {
  val active=renderer ?: return@LaunchedEffect
  runCatching { active.render(pageIndex,1400) }.onSuccess { bitmap=it; selected=null }
   .onFailure { error=it.message ?: "تعذّر عرض الصفحة" }
 }
 DisposableEffect(Unit) { onDispose { renderer?.close() } }
 Scaffold(containerColor=Color(0xFFF7F5EF),topBar={ CenterAlignedTopAppBar(title={ Text("مسعى",color=Color(0xFF174C3D)) }) }) { padding ->
  Column(Modifier.fillMaxSize().padding(padding).padding(horizontal=16.dp),horizontalAlignment=Alignment.CenterHorizontally) {
   Spacer(Modifier.height(8.dp))
   Button(onClick={ picker.launch(arrayOf("application/pdf")) }) { Text(if(uriText==null) "اختيار ملف PDF" else "تغيير الكتاب") }
   error?.let { Text(it,color=MaterialTheme.colorScheme.error,modifier=Modifier.padding(8.dp)) }
   if(uriText==null) {
    Spacer(Modifier.height(28.dp))
    Text("قارئ الرزم التعليمية",style=MaterialTheme.typography.headlineSmall,color=Color(0xFF174C3D))
    Text("افتح ملف الرزمة المحفوظ على جهازك لقراءته دون اتصال بعد اختياره.",modifier=Modifier.padding(12.dp),color=Color(0xFF55534D))
   } else if(bitmap!=null) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically) {
     Text("الصفحة ${pageIndex+1} من $pageCount",color=Color(0xFF44443F))
     Row {
      TextButton(onClick={ zoom=(zoom/1.2f).coerceAtLeast(1f) }) { Text("−") }
      TextButton(onClick={ zoom=(zoom*1.2f).coerceAtMost(4f) }) { Text("+") }
     }
    }
    val transform=rememberTransformableState { scaleChange,_,_ -> zoom=(zoom*scaleChange).coerceIn(1f,4f) }
    BoxWithConstraints(Modifier.weight(1f).fillMaxWidth().background(Color(0xFFE7E3D9)),contentAlignment=Alignment.Center) {
     val page=bitmap!!
     val imageHeight=maxWidth*(page.height.toFloat()/page.width.toFloat())
     Image(bitmap=page.asImageBitmap(),contentDescription="صفحة الكتاب ${pageIndex+1}",contentScale=ContentScale.Fit,
      modifier=Modifier.fillMaxWidth().height(imageHeight).transformable(transform)
       .pointerInput(pageIndex,page,zoom,uriText) {
        detectTapGestures { tap ->
         if(zoom<=1.02f) {
          val nx=(tap.x/size.width).coerceIn(0f,1f); val ny=(tap.y/size.height).coerceIn(0f,1f)
          val key=Uri.parse(uriText).lastPathSegment.orEmpty().substringBeforeLast('.').replace(Regex("[^A-Za-z0-9_-]"),"_")
          selected=resolveExactHit(loader.load(key,pageIndex),pageIndex,nx,ny)
         }
        }
       })
    }
    Row(Modifier.fillMaxWidth().padding(vertical=8.dp),horizontalArrangement=Arrangement.SpaceBetween) {
     OutlinedButton(enabled=pageIndex>0,onClick={ pageIndex--; zoom=1f }) { Text("السابق") }
     OutlinedButton(enabled=pageIndex<pageCount-1,onClick={ pageIndex++; zoom=1f }) { Text("التالي") }
    }
   }
  }
 }
 selected?.let { item ->
  ModalBottomSheet(onDismissRequest={ selected=null }) {
   Column(Modifier.fillMaxWidth().padding(22.dp)) {
    Text(item.title,style=MaterialTheme.typography.titleLarge,color=Color(0xFF174C3D))
    Text(item.type,style=MaterialTheme.typography.labelSmall,color=Color.Gray,modifier=Modifier.padding(top=4.dp,bottom=12.dp))
    Text(item.explanation,style=MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(24.dp))
   }
  }
 }
}
