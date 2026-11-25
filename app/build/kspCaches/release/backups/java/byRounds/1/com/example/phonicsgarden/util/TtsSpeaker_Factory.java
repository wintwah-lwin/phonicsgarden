package com.example.phonicsgarden.util;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class TtsSpeaker_Factory implements Factory<TtsSpeaker> {
  private final Provider<Context> contextProvider;

  public TtsSpeaker_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TtsSpeaker get() {
    return newInstance(contextProvider.get());
  }

  public static TtsSpeaker_Factory create(Provider<Context> contextProvider) {
    return new TtsSpeaker_Factory(contextProvider);
  }

  public static TtsSpeaker newInstance(Context context) {
    return new TtsSpeaker(context);
  }
}
