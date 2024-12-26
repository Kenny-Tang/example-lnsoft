import com.redjujubetree.example.SnowflakeIdGenerator;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {
		SnowflakeIdGenerator sequence = new SnowflakeIdGenerator(null);
		int i1 = 10000;
		ArrayList<Long> arrayList = new ArrayList<>(i1 * 2);
		for (int i = 0; i < 10000; i++) {
			arrayList.add(sequence.nextId());
		}

		Set<Long> collect = arrayList.parallelStream().filter(t -> {
			//System.out.println(t);
			return true;
		}).collect(Collectors.toSet());
		System.out.println(collect.size());

	}
}
