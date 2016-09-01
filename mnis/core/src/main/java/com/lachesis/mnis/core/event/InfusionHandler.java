package com.lachesis.mnis.core.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class InfusionHandler implements InvocationHandler{
		//目标对象
		private Object targetObject;
		/**
		 * 创建动态代理类
		 * @return object(代理类)
		 */
		public Object createProxy(Object targetObject){
			this.targetObject = targetObject;
			return Proxy.newProxyInstance(
					targetObject.getClass().getClassLoader(), 
						targetObject.getClass().getInterfaces(), this);
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Object obj = null;
			try {
				//obj： 目标对象--->代理对象的返回值--->返回给调用者的信息
				//this.invoke("目标对象","代理对象给目标对象传递参数");
				//调用目标对象中方法
				obj = method.invoke(targetObject, args);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return obj;
		}

}
