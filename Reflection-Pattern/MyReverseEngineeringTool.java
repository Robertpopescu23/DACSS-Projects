import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MyReverseEngineeringTool
{
	public static void main(String[] args) throws IOException
	{
		if(args.length < 1)
		{
			System.out.println("Usage: java MyReverseEngineeringTool <compiled-file.jar>");
			return;
		}

		String fileName = args[0];
		File inputFile = new File(fileName);

		if(!inputFile.exists())
		{
			System.out.println("File not found: " + inputFile.getAbsolutePath());
			return;
		}

		if(!inputFile.getName().endsWith(".jar"))
		{
			System.out.println("This tool currently supports only JAR files.");
			return;
		}
		analyzeJar(inputFile);
	}

	@SuppressWarnings("depercation")
	private static void analyzeJar(File inputFile) throws IOException
	{
		JarFile jarFile = new JarFile(inputFile);
		Enumeration<JarEntry> entries = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + inputFile.getAbsolutePath() + "!/") };
		URLClassLoader classLoader = URLClassLoader.newInstance(urls);

		while(entries.hasMoreElements())
		{
			JarEntry entry = entries.nextElement();

			if(entry.isDirectory() || !entry.getName().endsWith(".class"))
			{
				continue;
			}

			String className = entry.getName().replace("/", ".").replace(".class", "");

			try
			{
				Class<?> classObject = classLoader.loadClass(className);
				printClassInformation(classObject);
			}
			catch(Throwable e)
			{
				System.out.println("Could not load class: " + className);
				e.printStackTrace();
			}
		}
		jarFile.close();
	}

	private static void printClassInformation(Class<?> classObject)
	{
		System.out.println("Class: " + classObject.getName());

		Class<?> superClass = classObject.getSuperclass();
		if(superClass != null && superClass != Object.class)
		{
			System.out.println("  extends: " + superClass.getName());
		}

		Class<?>[] interfaces = classObject.getInterfaces();
		if(interfaces.length > 0)
		{
			System.out.println("  implements:");
			for(Class<?> iface : interfaces)
			{
				System.out.println("    " + iface.getName());
			}
		}

		System.out.println("  Fields:");
		for(Field f : classObject.getDeclaredFields())
		{
			System.out.println("    "  +
							   Modifier.toString(f.getModifiers()) +
							   " " +
							   f.getType().getSimpleName() +
							   " " + 
							   f.getName());
		}

		System.out.println("  Constructors:");
		for(Constructor<?> constructor : classObject.getDeclaredConstructors())
		{
			System.out.print("    " + Modifier.toString(constructor.getModifiers()) + " " + classObject.getSimpleName() + "(");
			Class<?>[] params = constructor.getParameterTypes();
			for(int index = 0; index < params.length; index++)
			{
				System.out.print(params[index].getSimpleName());
				if (index < params.length - 1) System.out.print(", ");
			}
			System.out.println(")");
		}

		System.out.println("  Methods:");
		for(Method m : classObject.getDeclaredMethods())
		{
			System.out.print("    " + Modifier.toString(m.getModifiers()) + " " + m.getReturnType().getSimpleName() + " " + m.getName() + "(");
			Class<?>[] params = m.getParameterTypes();
			for(int index = 0; index < params.length; index++)
			{
				System.out.print(params[index].getSimpleName());
				if (index < params.length - 1) System.out.print(", ");
			}
			System.out.println(")");
		}
	}
}
