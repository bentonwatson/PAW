package core;

/**
 * This class is an external iterator to support the naviation
 * of BigWordCollection.
 * 
 * The collection can not be modified. All the navigations are read-only.
 * This is an external iterator and hence multiple iterators can be generated.
 * @author srj
 *
 */
public class BWCIterator {
	
	private BigWordCollection bwc;
	private int currentIndex = 0;
	private int manyItems = 0;
	
	/**
	 * Constructor for holding the reference to a BigWordCollection.
	 * This collection will be navigated. 
	 * 
	 * @param some_bwc
	 */
	public BWCIterator(BigWordCollection some_bwc)
	{
		bwc = some_bwc;
		currentIndex = 0;
		manyItems = some_bwc.size();
	}
	
	/**
	 * For getting the current element
	 * @return
	 */
	public BigWord getCurrent()
	{
		return bwc.getBigWord(currentIndex);
	}

	/**
	 * For getting the previous element
	 * @return
	 */
	public BigWord getPrevious()
	{
		if (currentIndex == 0)
		{
			System.out.println("There is no previous");
			return getCurrent();
		}
		currentIndex--;
		return getCurrent();
		
	}
	
	/**
	 * For getting the next element
	 * @return
	 */
	public BigWord getNext()
	{
		if (currentIndex == manyItems-1)
		{
			System.out.println("There is no next");
			return getCurrent();
		}
		currentIndex++;
		return getCurrent();
		
	}
	
	/**
	 * For moving the pointer to a previous element.
	 * If we are already at the first element, then do nothing
	 * should check hasPrevious() before calling previous()
	 * @return
	 */
	public void previous()
	{
		if (currentIndex == 0)
		{
			System.out.println("There is no previous");
			return;
		}
		currentIndex--;		
	}
	
	/**
	 * For checking to see if there is a previous BigWord in the list
	 * @return false if currentIndex is the first index
	 */
	public boolean hasPrevious(){
		if (currentIndex == 0)
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * For moving the pointer to a next element.
	 * If we are already at the last element, then do nothing
	 * should check hasNext() before calling next()
	 * @return
	 */
	public void next()
	{
		if (currentIndex == manyItems-1)
		{
			System.out.println("There is no next");
			return;
		}
		currentIndex++;	
	}
	/**
	 * For checking to see if there is a next BigWord in the list
	 * @return false if currentIndex is the last index
	 */
	public boolean hasNext(){
		if (currentIndex == manyItems-1)
		{
			return false;
		}
		return true;
	}
	

	/**
	 * For moving the pointer to the start of the collection.
	 * @return
	 */
	public void start()
	{
		currentIndex = 0;
	}
	
	/**
	 * For moving the pointer to the end of the collection.
	 * @return
	 */
	public void end()
	{
		currentIndex = manyItems-1;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	   System.out.println("Generating a first Iterator on the first BWC");
       BigWordCollection bwc_1 = new BigWordCollection();
       BWCIterator bwc_1_iterator_1 = new BWCIterator(bwc_1);
       
       System.out.println(bwc_1_iterator_1.getCurrent());
       
       bwc_1_iterator_1.next();
       System.out.println(bwc_1_iterator_1.getCurrent());
       
       bwc_1_iterator_1.previous();
       System.out.println(bwc_1_iterator_1.getCurrent());
      
       bwc_1_iterator_1.previous();
       System.out.println(bwc_1_iterator_1.getCurrent());
      
       bwc_1_iterator_1.end();
       System.out.println(bwc_1_iterator_1.getCurrent());
       
       bwc_1_iterator_1.next();
       System.out.println(bwc_1_iterator_1.getCurrent());
       
       
       System.out.println("Generating a second Iterator on the first BWC");
       BWCIterator bwc_1_iterator_2= new BWCIterator(bwc_1);
       
       bwc_1_iterator_2.next();
       bwc_1_iterator_2.next();
       bwc_1_iterator_2.next();
       
       System.out.println(bwc_1_iterator_2.getCurrent());  
       bwc_1_iterator_2.end();
       System.out.println(bwc_1_iterator_2.getCurrent());

       bwc_1_iterator_2.start();
       System.out.println(bwc_1_iterator_2.getCurrent());
       
       bwc_1_iterator_2.next();
       System.out.println(bwc_1_iterator_2.getCurrent());
       
       
       System.out.println("Creating a mini collection for Presidents");
       System.out.println("Generating another iterator on thaat collection");
       BigWordCollection bwc_2 = new BigWordCollection();
       BigWordCollection presidents_bwc = bwc_2.getBigWordCollectionByKeyWord("Presidents");
       System.out.println("size of president's collection = " + presidents_bwc.size());
       
       BWCIterator p_bwc_iterator = new BWCIterator(presidents_bwc);
       
       System.out.println(p_bwc_iterator.getCurrent());
       
       p_bwc_iterator.next();
       System.out.println(p_bwc_iterator.getCurrent());
       
       p_bwc_iterator.previous();
       System.out.println(p_bwc_iterator.getCurrent());
      
       p_bwc_iterator.previous();
       System.out.println(p_bwc_iterator.getCurrent());
      
       p_bwc_iterator.end();
       System.out.println(p_bwc_iterator.getCurrent());
       
       p_bwc_iterator.next();
       System.out.println(p_bwc_iterator.getCurrent());
       
       
	}

}
