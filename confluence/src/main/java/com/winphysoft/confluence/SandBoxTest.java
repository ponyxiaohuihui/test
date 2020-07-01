package com.winphysoft.confluence;

import com.atlassian.confluence.util.sandbox.Sandbox;
import com.atlassian.confluence.util.sandbox.SandboxTask;

import java.time.Duration;

/**
 * @author pony
 * Created by pony on 2020/7/1
 */
public class SandBoxTest implements Sandbox {
    public <T, R> R execute(SandboxTask<T, R> sandboxTask, T t) {
        return null;
    }

    public <T, R> R execute(SandboxTask<T, R> sandboxTask, T t, Duration duration) {
        return null;
    }
}
