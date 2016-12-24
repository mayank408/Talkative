// Generated code from Butter Knife. Do not modify!
package com.example.mayank.abcd;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListenerActivity_ViewBinding<T extends ListenerActivity> implements Unbinder {
  protected T target;

  @UiThread
  public ListenerActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.btnSpeak = Utils.findRequiredViewAsType(source, R.id.btnSpeak, "field 'btnSpeak'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.btnSpeak = null;

    this.target = null;
  }
}
