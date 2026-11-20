package org.masaa.reader.model
import org.junit.Assert.*
import org.junit.Test
class InteractiveElementTest {
 private fun item(id:String,page:Int=0,left:Float=.1f,right:Float=.4f)=InteractiveElement(id,page,"paragraph",id,"verified",NormalizedRect(left,.1f,right,.4f))
 @Test fun exactHit(){ assertEquals("a",resolveExactHit(listOf(item("a")),0,.2f,.2f)?.id) }
 @Test fun outsideReturnsNull(){ assertNull(resolveExactHit(listOf(item("a")),0,.8f,.8f)) }
 @Test fun wrongPageReturnsNull(){ assertNull(resolveExactHit(listOf(item("a")),1,.2f,.2f)) }
 @Test fun overlapIsAmbiguous(){ assertNull(resolveExactHit(listOf(item("a"),item("b",left=.2f,right=.5f)),0,.3f,.2f)) }
}
