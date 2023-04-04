
public class MyTests {
	void abc() {
		System.out.println("abc");
	}
	
	void testInner(String str) {
		System.out.println(str);
	}
	
	void test1() {
	}
	
	void test2() {
	}
	
	void test3() {
		throw new RuntimeException("Test 3 exception");
	}
}
