
public class Pair<T1,T2> {
	private T1 t1;
	private T2 t2;
	
	public boolean isEqual(Pair<T1,T2> pair)
	{
		if(t1 == pair.t1 && t2 == pair.t2)
			return true;
		return false;
	}
}
