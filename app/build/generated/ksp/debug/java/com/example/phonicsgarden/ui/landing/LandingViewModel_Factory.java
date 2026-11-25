package com.example.phonicsgarden.ui.landing;

import com.example.phonicsgarden.data.repository.ProgressRepository;
import com.example.phonicsgarden.data.repository.WordRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class LandingViewModel_Factory implements Factory<LandingViewModel> {
  private final Provider<WordRepository> wordRepositoryProvider;

  private final Provider<ProgressRepository> progressRepositoryProvider;

  public LandingViewModel_Factory(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider) {
    this.wordRepositoryProvider = wordRepositoryProvider;
    this.progressRepositoryProvider = progressRepositoryProvider;
  }

  @Override
  public LandingViewModel get() {
    return newInstance(wordRepositoryProvider.get(), progressRepositoryProvider.get());
  }

  public static LandingViewModel_Factory create(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider) {
    return new LandingViewModel_Factory(wordRepositoryProvider, progressRepositoryProvider);
  }

  public static LandingViewModel newInstance(WordRepository wordRepository,
      ProgressRepository progressRepository) {
    return new LandingViewModel(wordRepository, progressRepository);
  }
}
