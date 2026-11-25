package com.example.phonicsgarden.engine;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
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
public final class ChallengeGenerator_Factory implements Factory<ChallengeGenerator> {
  @Override
  public ChallengeGenerator get() {
    return newInstance();
  }

  public static ChallengeGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ChallengeGenerator newInstance() {
    return new ChallengeGenerator();
  }

  private static final class InstanceHolder {
    private static final ChallengeGenerator_Factory INSTANCE = new ChallengeGenerator_Factory();
  }
}
