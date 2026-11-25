package com.example.phonicsgarden.di;

import com.example.phonicsgarden.data.local.AppDatabase;
import com.example.phonicsgarden.data.local.dao.MasteryDao;
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
public final class DatabaseModule_ProvideMasteryDaoFactory implements Factory<MasteryDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideMasteryDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public MasteryDao get() {
    return provideMasteryDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideMasteryDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideMasteryDaoFactory(dbProvider);
  }

  public static MasteryDao provideMasteryDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMasteryDao(db));
  }
}
