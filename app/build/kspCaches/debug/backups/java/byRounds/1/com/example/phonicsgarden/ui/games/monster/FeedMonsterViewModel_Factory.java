package com.example.phonicsgarden.ui.games.monster;

import com.example.phonicsgarden.data.repository.ProgressRepository;
import com.example.phonicsgarden.data.repository.SettingsRepository;
import com.example.phonicsgarden.data.repository.WordRepository;
import com.example.phonicsgarden.engine.WordChallengeGenerator;
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
public final class FeedMonsterViewModel_Factory implements Factory<FeedMonsterViewModel> {
  private final Provider<WordRepository> wordRepositoryProvider;

  private final Provider<ProgressRepository> progressRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<WordChallengeGenerator> generatorProvider;

  private final Provider<TtsSpeaker> speakerProvider;

  public FeedMonsterViewModel_Factory(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<WordChallengeGenerator> generatorProvider, Provider<TtsSpeaker> speakerProvider) {
    this.wordRepositoryProvider = wordRepositoryProvider;
    this.progressRepositoryProvider = progressRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.generatorProvider = generatorProvider;
    this.speakerProvider = speakerProvider;
  }

  @Override
  public FeedMonsterViewModel get() {
    return newInstance(wordRepositoryProvider.get(), progressRepositoryProvider.get(), settingsRepositoryProvider.get(), generatorProvider.get(), speakerProvider.get());
  }

  public static FeedMonsterViewModel_Factory create(Provider<WordRepository> wordRepositoryProvider,
      Provider<ProgressRepository> progressRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<WordChallengeGenerator> generatorProvider, Provider<TtsSpeaker> speakerProvider) {
    return new FeedMonsterViewModel_Factory(wordRepositoryProvider, progressRepositoryProvider, settingsRepositoryProvider, generatorProvider, speakerProvider);
  }

  public static FeedMonsterViewModel newInstance(WordRepository wordRepository,
      ProgressRepository progressRepository, SettingsRepository settingsRepository,
      WordChallengeGenerator generator, TtsSpeaker speaker) {
    return new FeedMonsterViewModel(wordRepository, progressRepository, settingsRepository, generator, speaker);
  }
}
