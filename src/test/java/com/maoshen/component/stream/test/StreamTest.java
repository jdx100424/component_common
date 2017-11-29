package com.maoshen.component.stream.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONObject;

public class StreamTest {
	public static void main(String[] s) {
		// 清单 4. 构造流的几种常见方法
		Stream<String> stream = Stream.of("a", "b", "c");
		Stream<String> stream1 = Stream.of("a", "b", "c");
		// 2. Arrays
		String[] strArray = new String[] { "a", "b", "c" };
		stream = Stream.of(strArray);
		stream = Arrays.stream(strArray);
		// 3. Collections
		List<String> list = Arrays.asList(strArray);
		stream = list.stream();
		System.out.println(JSONObject.toJSONString(stream));

		// 清单 5. 数值流的构造
		IntStream.of(new int[] { 1, 2, 3 }).forEach(System.out::println);
		IntStream.range(1, 3).forEach(System.out::println);
		IntStream.rangeClosed(1, 3).forEach(System.out::println);

		// 清单 6. 流转换为其它数据结构
		// 1. Array
		String[] strArray1 = stream1.toArray(String[]::new);
		// 2. Collection
		stream1 = Stream.of("a", "b", "c");
		List<String> list1 = stream1.collect(Collectors.toList());
		stream1 = Stream.of("a", "b", "c");
		List<String> list2 = stream1.collect(Collectors.toCollection(ArrayList::new));
		stream1 = Stream.of("a", "b", "c");
		Set<String> set1 = stream1.collect(Collectors.toSet());
		stream1 = Stream.of("a", "b", "c");
		Stack<String> stack1 = stream1.collect(Collectors.toCollection(Stack::new));
		// 3. String
		stream1 = Stream.of("a", "b", "c");
		String str = stream1.collect(Collectors.joining()).toString();
		System.out.println(JSONObject.toJSONString(strArray1));
		System.out.println(JSONObject.toJSONString(list1));
		System.out.println(JSONObject.toJSONString(list2));
		System.out.println(JSONObject.toJSONString(set1));
		System.out.println(JSONObject.toJSONString(stack1));
		System.out.println(JSONObject.toJSONString(str));

		// 清单 8. 平方数
		List<Integer> nums = Arrays.asList(1, 2, 3, 4);
		List<Integer> squareNums = nums.stream().map(n -> n * n).collect(Collectors.toList());
		System.out.println(JSONObject.toJSONString(squareNums));

		// 清单 9. 一对多
		Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
		Stream<Integer> outputStream = inputStream.flatMap((childList) -> childList.stream());
		System.out.println(JSONObject.toJSONString(outputStream.collect(Collectors.toList())));

		// filter 对原始 Stream 进行某项测试，通过测试的元素被留下来生成一个新 Stream。
		// 清单 10. 留下偶数
		Integer[] sixNums = { 1, 2, 3, 4, 5, 6 };
		Integer[] evens = Stream.of(sixNums).filter(n -> n % 2 == 0).toArray(Integer[]::new);
		System.out.println("清单 10. 留下偶数:" + JSONObject.toJSONString(evens));

		//清单 15. reduce 的用例
		// 字符串连接，concat = "ABCD"
		String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
		// 求最小值，minValue = -3.0
		double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
		// 求和，sumValue = 10, 有起始值
		int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
		// 求和，sumValue = 10, 无起始值
		int sumValue1 = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
		// 过滤，字符串连接，concat = "ace"
		String concat1 = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0).reduce("", String::concat);
		System.out.println("清单 15. reduce 的用例:"+minValue+","+concat+","+sumValue + ","+sumValue1 + ","+ concat1);
	}
}
