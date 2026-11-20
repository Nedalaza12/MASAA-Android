package org.masaa.reader.model
data class NormalizedRect(val left:Float,val top:Float,val right:Float,val bottom:Float) {
 init { require(left in 0f..1f && top in 0f..1f && right in 0f..1f && bottom in 0f..1f); require(left<right && top<bottom) }
 fun contains(x:Float,y:Float)=x>=left && x<=right && y>=top && y<=bottom
}
data class InteractiveElement(val id:String,val pageIndex:Int,val type:String,val title:String,val explanation:String,val bounds:NormalizedRect)
fun resolveExactHit(elements:List<InteractiveElement>,pageIndex:Int,x:Float,y:Float):InteractiveElement? {
 val hits=elements.filter { it.pageIndex==pageIndex && it.bounds.contains(x,y) }
 return hits.singleOrNull()
}
