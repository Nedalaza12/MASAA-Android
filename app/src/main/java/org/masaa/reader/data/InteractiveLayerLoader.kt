package org.masaa.reader.data
import android.content.Context
import org.json.JSONObject
import org.masaa.reader.model.InteractiveElement
import org.masaa.reader.model.NormalizedRect
class InteractiveLayerLoader(private val context:Context) {
 fun load(bookKey:String,pageIndex:Int):List<InteractiveElement> = runCatching {
  val raw=context.assets.open("interactive/$bookKey/p${pageIndex+1}.json").bufferedReader().use { it.readText() }
  val array=JSONObject(raw).optJSONArray("elements") ?: return emptyList()
  buildList {
   for(i in 0 until array.length()) {
    val e=array.optJSONObject(i) ?: continue; val b=e.optJSONObject("bounds") ?: continue
    val title=e.optString("title").trim(); val explanation=e.optString("explanation").trim()
    if(title.isBlank() || explanation.isBlank()) continue
    runCatching { add(InteractiveElement(e.getString("id"),pageIndex,e.optString("type","content"),title,explanation,NormalizedRect(b.getDouble("left").toFloat(),b.getDouble("top").toFloat(),b.getDouble("right").toFloat(),b.getDouble("bottom").toFloat()))) }
   }
  }
 }.getOrDefault(emptyList())
}
