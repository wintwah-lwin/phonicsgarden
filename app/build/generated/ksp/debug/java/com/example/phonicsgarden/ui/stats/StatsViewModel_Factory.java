package com.example.phonicsgarden.ui.stats;

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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<WordRepository> wordRepositoryProvider;

  private final Provider<ProgressRepository> progressRepositoryProvider;

  public StatsViewModel_Factory(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider) {
    this.wordRepositoryProvider = wordRepositoryProvider;
    this.progressRepositoryProvider = progressRepositoryProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(wordRepositoryProvider.get(), progressRepositoryProvider.get());
  }

  public static StatsViewModel_Factory create(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider) {
    return new StatsViewModel_Factory(wordRepositoryProvider, progressRepositoryProvider);
  }

  public static StatsViewModel newInstance(WordRepository wordRepository,
      ProgressRepository progressRepository) {
    return new StatsViewModel(wordRepository, progressRepository);
  }
}
