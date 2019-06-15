package lrm.com.api;

public class LrmJector {
    public static void bind(Object activity) {
        bind(activity, activity);
    }

    /**
     * 通过反射拿到生成的文件类类对象，并初始化实例
     * 调用实例方法 达到findViewById的效果
     * @param host
     * @param root
     */
    public static void bind(Object host, Object root) {
        Class<?> clazz = host.getClass();
        String proxyClassFullName = clazz.getName() + "ViewInjector";
        try {
            //反射拿到生成的类
            Class<?> proxyClazz = Class.forName(proxyClassFullName);
            //实例化当前类实例
            ViewInjector viewInjector = (ViewInjector) proxyClazz.newInstance();
            //调用方法
            viewInjector.inject(host, root);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
