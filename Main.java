import java.util.ArrayList;

import SmartRandom.SmartRandom;
import Taipan.*;

class Main {
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		try {
			new Game<SmartRandom, SmartRandom, SmartRandom, SmartRandom>(SmartRandom.class, SmartRandom.class, SmartRandom.class, SmartRandom.class).play();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		//test();
	}

	void test() {
		class Test {
			String s;
			int soFar;

			Test(String s1, int soFar1) {
				s = s1;
				soFar = soFar1;
			}
		}
		ArrayList<Test> t = new ArrayList<Test>();
		t.add(new Test("0", 0));
		t.add(new Test("1", 1));
		t.add(new Test("2", 2));
		for (int i = 1; i < 10; i++) {
			ArrayList<Test> t2 = new ArrayList<Test>();
			for (Test s : t) {
				/*t2.add(new Test(s.s + "0", s.soFar + 0));
				if (s.soFar <= 4) t2.add(new Test(s.s + "1", s.soFar + 1));
				if (s.soFar <= 3) t2.add(new Test(s.s + "2", s.soFar + 2));*/
				t2.add(new Test(s.s + "0", s.soFar + 0));
				if (s.s.charAt(s.s.length() - 1) != '1') t2.add(new Test(s.s + "1", s.soFar + 1));
				t2.add(new Test(s.s + "2", s.soFar + 2));
			}
			t = t2;
		}
		int k = 0;
		for (Test s : t) {
			if (s.soFar == 5 || true) {
				System.out.println(s.s + " " + s.soFar);
				k++;
			}
		}
		System.out.println(k);
	}
}

