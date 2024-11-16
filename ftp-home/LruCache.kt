
//import kotlin.system.measureTimedValue


class LruCache<K, T> {
   private val disk = HashMap<K, T>()
   private val useFrequency = mutableListOf<K>()
   private var cachedSize = 0
    
    fun add(key: K, data: T) {
        // remove the least use item
        if (disk.size >= cachedSize) remove()
        
        
        // add the item
        disk[key] = data
        useFrequency.add(0, key)
        
    }
    
    fun get(key: K): T?{
        // add the key to the top of the useFrequency
        useFrequency.remove(key)
        useFrequency.add(0, key)
        
        return disk.get(key)
    }
    
    private fun remove() {
        disk.remove(useFrequency[useFrequency.size - 1])
        useFrequency.removeAt(useFrequency.size - 1)
    }
    
    fun setCachedSize(size: Int) { cachedSize = size }
    
    fun showCache() {
        println(disk)
    }
}


fun main(args: Array<String>) {
    val cache = LruCache<Int, String>()
    
    val list = mutableListOf<String>()
    val charList = mutableListOf<Char>()

    
    args.joinToString().forEach { char ->
        if (charList.size >= (3..5).random()) {
            list.add(charList.joinToString())
            charList.clear()
        }else {
            charList.add(char)
        }
    }
    
    list.map {
       list.indexOf(it).toString() + ",$it"
    }.shuffled().forEach { keyValue ->
        val keyAndValue = keyValue.split(",")
        cache.add(keyAndValue[0].toInt(), keyAndValue[1])
    }
    
    cache.showCache()
    
    //test(emptyList(), cache)
    test2()
}


fun test(items: List<String>, cache: LruCache<Int, String>) {

    val storage = mutableMapOf<Int, String>()
    
    repeat(50000) {
        storage[it] = ('A'..'Z').random().toString() + ('a'..'z').random().toString()
    }
    

    while(true) {

    
        print("Enter a key ")
        val rl = readLine()!!
        
        val (time, itemFromChache) = measureTimedValue {
            cache.get(rl.toInt())
        }
        //val itemFromChache = cache.get(rl.toInt())
        
        if (itemFromChache != null) {
            println("item from cache!: $itemFromChache took $time ms")
        }else {
            println("Item not in cache, getting it from storage")
           // val itFrStorage = storage[rl.toInt()
            
            val (time2, itFrStorage) = measureTimedValue {
                storage[rl.toInt()]
            }
            
            if (itFrStorage != null) {
               println("item from storage!: $itFrStorage took $time2 ms") 
               print("Caching the it for next time..")
               cache.add(rl.toInt(), itFrStorage)
               if(cache.get(rl.toInt()) != null) {
                   print(".. \nItem was cached successfully\n")
               } else {
                   println("Error occurred while caching item")
               }
            }else {
                println("Item not found!")
            }
        }
    } 
}


data class TimeValue<T>(
  val time: Long,
  val value: T
)

fun <T> measureTimedValue(block: () -> T): TimeValue<T> {
    val start = System.currentTimeMillis()
    val result = block()
    
    return TimeValue(
        System.currentTimeMillis() - start,
        result 
    )
}

data class Test(val id: Int, val data: String)


fun test2() {
    val storage = mutableListOf<Test>()
    val cache = LruCache<Int, Test>()
    
    val storageSize = validate("Enter storage size")
    cache.setCachedSize(validate("Enter cached size"))
    
    repeat(storageSize) {
        storage.add(Test(it, ('A'..'Z').random().toString() + ('a'..'z').random().toString()))
    }
    
    
    while(true) {
    
      try {
        print("\nEnter a key ")
        val rl = readLine()!!
        
        if (rl == "sv") cache.showCache()
        
        
        val (time, itemFromChache) = measureTimedValue {
            cache.get(rl.toInt())
        }
        //val itemFromChache = cache.get(rl.toInt())
        
        if (itemFromChache != null) {
            println("item from cache!: $itemFromChache took $time ms")
            
            
        }else {
            println("Item not in cache, getting it from storage")
           // val itFrStorage = storage[rl.toInt()
            
            val (time2, itFrStorage) = measureTimedValue {
                storage.find { it.id == rl.toInt()}
            }
            
            if (itFrStorage != null) {
               println("item from storage!: $itFrStorage took $time2 ms") 
               print("Caching the it for next time..")
               cache.add(rl.toInt(), itFrStorage)
               if(cache.get(rl.toInt()) != null) {
                   print(".. \nItem was cached successfully\n")
               } else {
                   println("Error occurred while caching item")
               }
            }else {
                println("Item not found!")
            }
        } 
      } catch (_: Exception) {}
    } 
}


fun validate(prompt: String): Int{
    print("$prompt: ")
    while(true) {
        try {
            val rl = readLine()!!
            if (rl.toInt() > 0) return rl.toInt()
        }catch (_: Exception) { print("Invalid Input. try again ")}
    }
}