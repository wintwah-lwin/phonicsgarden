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
public final class WordChallengeGenerator_Factory implements Factory<WordChallengeGenerator> {
  @Override
  public WordChallengeGenerator get() {
    return newInstance();
  }

  public static WordChallengeGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static WordChallengeGenerator newInstance() {
    return new WordChallengeGenerator();
  }

  private static final class InstanceHolder {
    private static final WordChallengeGenerator_Factory INSTANCE = new WordChallengeGenerator_Factory();
  }
}
