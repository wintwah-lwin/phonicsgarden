package com.example.phonicsgarden.ui.games.tracing;

import com.example.phonicsgarden.data.repository.ProgressRepository;
import com.example.phonicsgarden.data.repository.SettingsRepository;
import com.example.phonicsgarden.data.repository.WordRepository;
import com.example.phonicsgarden.util.TtsSpeaker;
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
public final class TracingViewModel_Factory implements Factory<TracingViewModel> {
  private final Provider<WordRepository> wordRepositoryProvider;

  private final Provider<ProgressRepository> progressRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<TtsSpeaker> speakerProvider;

  public TracingViewModel_Factory(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<TtsSpeaker> speakerProvider) {
    this.wordRepositoryProvider = wordRepositoryProvider;
    this.progressRepositoryProvider = progressRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.speakerProvider = speakerProvider;
  }

  @Override
  public TracingViewModel get() {
    return newInstance(wordRepositoryProvider.get(), progressRepositoryProvider.get(), settingsRepositoryProvider.get(), speakerProvider.get());
  }

  public static TracingViewModel_Factory create(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<TtsSpeaker> speakerProvider) {
    return new TracingViewModel_Factory(wordRepositoryProvider, progressRepositoryProvider, settingsRepositoryProvider, speakerProvider);
  }

  public static TracingViewModel newInstance(WordRepository wordRepository,
      ProgressRepository progressRepository, SettingsRepository settingsRepository,
      TtsSpeaker speaker) {
    return new TracingViewModel(wordRepository, progressRepository, settingsRepository, speaker);
  }
}
