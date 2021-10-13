package executor;

import testcases.IMDBTest001;
import testcases.IMDBTest002;
import testcases.IMDBTest003;

public class Tests {
	
	/*** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * Function 			- Return the object of that selected Module class
	 * @param ModuleName 	- Module name
	 * @return 				- Object of module class
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    
	public TestInterface testModulesSelection(String TestName) {
		if(TestName.equalsIgnoreCase("IMDBTest001"))
			return new IMDBTest001();
//		else if(TestName.equalsIgnoreCase("IMDBTest002"))
//			return new IMDBTest002();
//		else if(TestName.equalsIgnoreCase("IMDBTest003"))
//			return new IMDBTest003();
		else
			return null;
	}
}
