package com.example.phonicsgarden.di;

import com.example.phonicsgarden.data.local.AppDatabase;
import com.example.phonicsgarden.data.local.dao.AttemptDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAttemptDaoFactory implements Factory<AttemptDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideAttemptDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AttemptDao get() {
    return provideAttemptDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAttemptDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideAttemptDaoFactory(dbProvider);
  }

  public static AttemptDao provideAttemptDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAttemptDao(db));
  }
}
