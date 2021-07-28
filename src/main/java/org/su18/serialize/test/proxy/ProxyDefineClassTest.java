package org.su18.serialize.test.proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * 直接反射调用 Proxy 的 native defineClass0 方法加载类字节码
 *
 * @author su18
 */
public class ProxyDefineClassTest {

	public static String CLASS_NAME = "org.su18.serialize.test.proxy.SuTestClass";


	public static byte[] CLASS_BYTES = new byte[]{-54, -2, -70, -66, 0, 0, 0, 51, 0, 20, 10, 0, 4, 0, 16, 8, 0, 17, 7,
			0, 18, 7, 0, 19, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0,
			15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86,
			97, 114, 105, 97, 98, 108, 101, 84, 97, 98, 108, 101, 1, 0, 4, 116, 104, 105, 115, 1, 0, 43, 76, 111, 114,
			103, 47, 115, 117, 49, 56, 47, 115, 101, 114, 105, 97, 108, 105, 122, 101, 47, 116, 101, 115, 116, 47, 112,
			114, 111, 120, 121, 47, 83, 117, 84, 101, 115, 116, 67, 108, 97, 115, 115, 59, 1, 0, 6, 99, 97, 108, 108,
			77, 101, 1, 0, 20, 40, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59,
			1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 16, 83, 117, 84, 101, 115, 116, 67, 108, 97,
			115, 115, 46, 106, 97, 118, 97, 12, 0, 5, 0, 6, 1, 0, 43, 67, 97, 108, 108, 32, 77, 101, 32, 66, 121, 32,
			89, 111, 117, 114, 32, 72, 101, 97, 114, 116, 44, 65, 110, 100, 32, 73, 32, 83, 104, 97, 108, 108, 32, 66,
			101, 32, 84, 104, 101, 114, 101, 46, 1, 0, 41, 111, 114, 103, 47, 115, 117, 49, 56, 47, 115, 101, 114, 105,
			97, 108, 105, 122, 101, 47, 116, 101, 115, 116, 47, 112, 114, 111, 120, 121, 47, 83, 117, 84, 101, 115, 116,
			67, 108, 97, 115, 115, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 0,
			33, 0, 3, 0, 4, 0, 0, 0, 0, 0, 2, 0, 1, 0, 5, 0, 6, 0, 1, 0, 7, 0, 0, 0, 47, 0, 1, 0, 1, 0, 0, 0, 5, 42,
			-73, 0, 1, -79, 0, 0, 0, 2, 0, 8, 0, 0, 0, 6, 0, 1, 0, 0, 0, 6, 0, 9, 0, 0, 0, 12, 0, 1, 0, 0, 0, 5, 0, 10,
			0, 11, 0, 0, 0, 9, 0, 12, 0, 13, 0, 1, 0, 7, 0, 0, 0, 27, 0, 1, 0, 0, 0, 0, 0, 3, 18, 2, -80, 0, 0, 0, 1, 0,
			8, 0, 0, 0, 6, 0, 1, 0, 0, 0, 9, 0, 1, 0, 14, 0, 0, 0, 2, 0, 15
	};


	public static Class<?> defineByProxy(String className, byte[] classBytes) throws Exception {

		// 获取系统的类加载器，可以根据具体情况换成一个存在的类加载器
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		// 反射java.lang.reflect.Proxy类获取其中的defineClass0方法
		Method method = Proxy.class.getDeclaredMethod("defineClass0",
				ClassLoader.class, String.class, byte[].class, int.class, int.class);
		// 修改方法的访问权限
		method.setAccessible(true);

		// 反射调用java.lang.reflect.Proxy.defineClass0()方法，动态向JVM注册对象
		// 返回一个 Class 对象
		return (Class<?>) method.invoke(null, classLoader, className, classBytes, 0, classBytes.length);
	}


	public static void main(String[] args) throws Exception {
		// 生成
//		readFileToBytes();

		Class<?> testClass = defineByProxy(CLASS_NAME, CLASS_BYTES);

		Method m = testClass.getDeclaredMethod("callMe");
		System.out.println(m.invoke(testClass));

	}


	public static void readFileToBytes() throws IOException {
		String filePath = "/Users/phoebe/IdeaProjects/ysoserial-su18/target/classes/org/su18/serialize/test/proxy/SuTestClass.class";
		System.out.println(Arrays.toString(Files.readAllBytes((new File(filePath)).toPath())));
	}


}


