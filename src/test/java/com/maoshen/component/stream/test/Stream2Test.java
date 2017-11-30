package com.maoshen.component.stream.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONObject;

public class Stream2Test {
	public static void main(String[] s) {
		Stream2Test stream2Test = new Stream2Test();
		// 2.1 获取流
		// 集合
		List<Person> list = new ArrayList<Person>();
		List<Person> list2 = new ArrayList<Person>();
		Person p = stream2Test.new Person();
		p.setId(1L);
		p.setName("p");
		p.setStudent(false);
		Person p2 = stream2Test.new Person();
		p2.setId(2L);
		p2.setName("p2");
		p2.setStudent(true);
		Person p3 = stream2Test.new Person();
		p3.setId(3L);
		p3.setName("p3");
		p3.setStudent(true);
		Person p4 = stream2Test.new Person();
		p4.setId(3L);
		p4.setName("p3");
		p4.setStudent(true);
		
		Person pp = stream2Test.new Person();
		pp.setId(1L);
		pp.setName("p");
		pp.setStudent(false);
		Person pp2 = stream2Test.new Person();
		pp2.setId(2L);
		pp2.setName("p2");
		pp2.setStudent(true);
		Person pp3 = stream2Test.new Person();
		pp3.setId(3L);
		pp3.setName("p3");
		pp3.setStudent(true);
		Person pp4 = stream2Test.new Person();
		pp4.setId(3L);
		pp4.setName("p3");
		pp4.setStudent(true);
		
		list.add(p);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list2.add(pp);
		list2.add(pp2);
		list2.add(pp3);
		list2.add(pp4);

		Stream<Person> stream = list.stream();

		// 数组
		String[] names = { "chaimm", "peter", "john", "john" };
		Stream<String> stream2 = Arrays.stream(names);

		// 值
		Stream<String> stream3 = Stream.of("chaimm", "peter", "john");

		// 2.2
		// 筛选filter,filter函数接收一个Lambda表达式作为参数，该表达式返回boolean，在执行过程中，流将元素逐一输送给filter，并筛选出执行结果为true的元素。
		// 如，筛选出所有学生：
		List<Person> resultFilter = list.stream().filter(ppp -> ppp.isStudent()).collect(Collectors.toList());
		System.out.println(JSONObject.toJSONString(resultFilter));
		
		Stream<Person> jdxTest = list2.stream();
		jdxTest.forEach(gogo -> gogo.setName(gogo.getName()+"wocao"));
		System.out.println("wocao:"+JSONObject.toJSONString(list2));
	

		// 2.3 去重distinct,去掉重复的结果：
		List<String> resultDistinct = Arrays.stream(names).distinct().collect(Collectors.toList());
		System.out.println(JSONObject.toJSONString(resultDistinct));

		// 2.4 截取,截取流的前N个元素：
		List<Person> resultLimit = list.stream().limit(2).collect(Collectors.toList());
		System.out.println(JSONObject.toJSONString(resultLimit));

		// 2.5 跳过,跳过流的前n个元素：
		List<Person> resultSkip = list.stream().skip(1).collect(Collectors.toList());
		System.out.println(JSONObject.toJSONString(resultSkip));

		// 2.6
		// 映射,对流中的每个元素执行一个函数，使得元素转换成另一种类型输出。流会将每一个元素输送给map函数，并执行map中的Lambda表达式，最后将执行结果存入一个新的流中。
		// 如，获取每个人的姓名(实则是将Perosn类型转换成String类型)：
		List<String> result = list.stream().map(Person::getName).collect(Collectors.toList());
		System.out.println(JSONObject.toJSONString(result));

		// 2.7 合并多个流，例：列出List中各不相同的单词，List集合如下：
		List<String> listMerge = new ArrayList<String>();
		listMerge.add("I am a boy");
		listMerge.add("I love the girl");
		listMerge.add("But the girl loves another girl");

		listMerge.stream().map(line -> line.split(" "));
		listMerge.stream().map(line -> line.split(" ")).map(Arrays::stream);
		listMerge.stream().map(line -> line.split(" ")).flatMap(Arrays::stream);
		List<String> resultMerge = listMerge.stream().map(line -> line.split(" ")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
		System.out.println(JSONObject.toJSONString(resultMerge));
	
		//2.8 是否匹配任一元素：anyMatch,anyMatch用于判断流中是否存在至少一个元素满足指定的条件，这个判断条件通过Lambda表达式传递给anyMatch，执行结果为boolean类型。 如，判断list中是否有学生：
		boolean resultAnyMatch = list.stream().anyMatch(Person::isStudent);
		System.out.println(JSONObject.toJSONString(resultAnyMatch));
		
		//2.9 是否匹配所有元素：allMatch,allMatch用于判断流中的所有元素是否都满足指定条件，这个判断条件通过Lambda表达式传递给anyMatch，执行结果为boolean类型。 如，判断是否所有人都是学生：
		boolean resultAllMatch = list.stream().allMatch(Person::isStudent);
		System.out.println(JSONObject.toJSONString(resultAllMatch));

		//2.10 是否未匹配所有元素：noneMatch
		boolean resultNoneMatch = list.stream().noneMatch(Person::isStudent);
		System.out.println(JSONObject.toJSONString(resultNoneMatch));

		//2.11 获取任一元素findAny
		Optional<Person> person = list.stream().findAny();
		Optional<Person> person2 = list.stream().findFirst();
		System.out.println(JSONObject.toJSONString(person.get()));
		System.out.println(JSONObject.toJSONString(person2.get()));
		
		//2.13.1 元素求和：自定义Lambda表达式实现求和
		Optional<Person> resultOp = list.stream().reduce((personOne,personTwo)-> stream2Test.new Person(personOne.getId()+personTwo.getId()));
		System.out.println(JSONObject.toJSONString(resultOp.get()));
		List<Long> resultIdList = list.stream().map(Person::getId).collect(Collectors.toList());
		Long longResult = resultIdList.stream().reduce(0L, (personOne,personTwo)->personOne + personTwo);
		System.out.println(JSONObject.toJSONString(longResult));

		long idLong = resultIdList.stream().reduce(0L, Long::sum);
		System.out.println(JSONObject.toJSONString(idLong));
		
		//2.14.1 将普通流转换成数值流
		LongStream streamLong = list.stream().mapToLong(Person::getId);
		//2.14.2 数值计算
		OptionalLong maxAge = list.stream().mapToLong(Person::getId).max();
		System.out.println(maxAge.getAsLong());
		
		//假设我们现在要统计一个List<Person>里面的男性个数，那么代码我们通常是这样写的。
		//是否学生
		long personCount = list.stream().filter(personCountObj -> personCountObj.isStudent()).count();  
		System.out.println("学生人数："+personCount);
		
		//我们需要把里面的字母全部转换成大写，在Java8里，我们可以利用Steam这样写代码。
		List<String> personList = list.stream().map(personString -> personString.setName(personString.getName()+"jdxGoGo")).collect(Collectors.toList()); 
		System.out.println(JSONObject.toJSONString(personList));
	}

	public class Person {
		private Long id;
		private String name;
		private boolean isStudent;

		public Person() {

		}
		
		public Person(Long id){
			this.id = id;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public String setName(String name) {
			this.name = name;
			return name;
		}

		public boolean isStudent() {
			return isStudent;
		}

		public void setStudent(boolean isStudent) {
			this.isStudent = isStudent;
		}
	}
}
