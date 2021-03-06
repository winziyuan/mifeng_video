package org.xutils.common.task;

import java.util.concurrent.Executor;

import org.xutils.common.Callback;


/**
 * Created by wyouflf on 15/6/5.
 * 异步任务基类
 *
 * @param <ResultType>
 */
public abstract class AbsTask<ResultType> implements Callback.Cancelable {

    private TaskProxy taskProxy = null;
    private final Callback.Cancelable cancelHandler;

    private volatile boolean isCancelled = false;
    private volatile State state = State.IDLE;
    private ResultType result;

    public AbsTask() {
        this(null);
    }

    public AbsTask(Callback.Cancelable cancelHandler) {
        this.cancelHandler = cancelHandler;
    }

    protected abstract ResultType doBackground() throws Throwable;

    protected abstract void onSuccess(ResultType result);

    protected abstract void onError(Throwable ex, boolean isCallbackError);

    protected void onWaiting() {
    }

    protected void onStarted() {
    }

    protected void onUpdate(int flag, Object... args) {
    }

    protected void onCancelled(Callback.CancelledException cex) {
    }

    protected void onFinished() {
    }

    public Priority getPriority() {
        return null;
    }

    public Executor getExecutor() {
        return null;
    }

    protected final void update(int flag, Object... args) {
        if (taskProxy != null) {
            taskProxy.onUpdate(flag, args);
        }
    }

    /**
     * invoked via cancel()
     */
    protected void cancelWorks() {
    }

    @Override
    public final void cancel() {
        if (!this.isCancelled) {
            this.isCancelled = true;
            this.state = State.CANCELLED;
            if (cancelHandler != null) {
                cancelHandler.cancel();
            }
            cancelWorks();
        }
    }

    @Override
    public final boolean isCancelled() {
        return isCancelled || state == State.CANCELLED ||
                (cancelHandler != null && cancelHandler.isCancelled());
    }

    public final boolean isFinished() {
        return this.state.value() > State.STARTED.value();
    }

    public final State getState() {
        return state;
    }

    public final ResultType getResult() {
        return result;
    }

    /*package*/
    final void setTaskProxy(TaskProxy taskProxy) {
        this.taskProxy = taskProxy;
    }

    /*package*/
    final void setState(State state) {
        this.state = state;
    }

    /*package*/
    final void setResult(ResultType result) {
        this.result = result;
    }

    /*package*/
    final Callback.Cancelable getCancelHandler() {
        return cancelHandler;
    }

    public enum State {
        IDLE(0), WAITING(1), STARTED(2), SUCCESS(3), CANCELLED(4), ERROR(5);
        private final int value;

        private State(int value) {
            this.value = value;
        }
        public static State valueOf(int value) {
            switch (value) {
                case 0:
                    return IDLE;
                case 1:
                    return WAITING;
                case 2:
                    return STARTED;
                case 3:
                    return SUCCESS;
                case 4:
                    return CANCELLED;
                case 5:
                    return ERROR;
                default:
                    return IDLE;
            }
        }
        public int value() {
            return value;
        }
    }
}
