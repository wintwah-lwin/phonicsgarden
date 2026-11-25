package com.example.phonicsgarden.data.repository;

import com.example.phonicsgarden.data.local.dao.AttemptDao;
import com.example.phonicsgarden.data.local.dao.MasteryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ProgressRepositoryImpl_Factory implements Factory<ProgressRepositoryImpl> {
  private final Provider<AttemptDao> attemptDaoProvider;

  private final Provider<MasteryDao> masteryDaoProvider;

  public ProgressRepositoryImpl_Factory(Provider<AttemptDao> attemptDaoProvider,
      Provider<MasteryDao> masteryDaoProvider) {
    this.attemptDaoProvider = attemptDaoProvider;
    this.masteryDaoProvider = masteryDaoProvider;
  }

  @Override
  public ProgressRepositoryImpl get() {
    return newInstance(attemptDaoProvider.get(), masteryDaoProvider.get());
  }

  public static ProgressRepositoryImpl_Factory create(Provider<AttemptDao> attemptDaoProvider,
      Provider<MasteryDao> masteryDaoProvider) {
    return new ProgressRepositoryImpl_Factory(attemptDaoProvider, masteryDaoProvider);
  }

  public static ProgressRepositoryImpl newInstance(AttemptDao attemptDao, MasteryDao masteryDao) {
    return new ProgressRepositoryImpl(attemptDao, masteryDao);
  }
}
