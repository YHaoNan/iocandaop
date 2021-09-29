package io.lilpig.iocaop.framework.aop;

public class DefaultMethodProxy implements MethodProxy{

    private final MethodProxyInvokeListener proxyInvokeListener;

    public DefaultMethodProxy(MethodProxyInvokeListener invokeListener) {
        this.proxyInvokeListener = invokeListener;
    }

    @Override
    public Object invoke() {
        return proxyInvokeListener.onMethodProxyInvoked();
    }
}
