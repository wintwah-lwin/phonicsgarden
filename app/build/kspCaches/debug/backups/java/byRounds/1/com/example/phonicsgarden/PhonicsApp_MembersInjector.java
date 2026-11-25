package com.example.phonicsgarden;

import com.example.phonicsgarden.data.repository.WordRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PhonicsApp_MembersInjector implements MembersInjector<PhonicsApp> {
  private final Provider<WordRepository> wordRepositoryProvider;

  public PhonicsApp_MembersInjector(Provider<WordRepository> wordRepositoryProvider) {
    this.wordRepositoryProvider = wordRepositoryProvider;
  }

  public static MembersInjector<PhonicsApp> create(
      Provider<WordRepository> wordRepositoryProvider) {
    return new PhonicsApp_MembersInjector(wordRepositoryProvider);
  }

  @Override
  public void injectMembers(PhonicsApp instance) {
    injectWordRepository(instance, wordRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.example.phonicsgarden.PhonicsApp.wordRepository")
  public static void injectWordRepository(PhonicsApp instance, WordRepository wordRepository) {
    instance.wordRepository = wordRepository;
  }
}
