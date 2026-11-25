package com.example.phonicsgarden.di;

import com.example.phonicsgarden.data.local.AppDatabase;
import com.example.phonicsgarden.data.local.dao.GraphemeDao;
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
public final class DatabaseModule_ProvideGraphemeDaoFactory implements Factory<GraphemeDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideGraphemeDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public GraphemeDao get() {
    return provideGraphemeDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideGraphemeDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideGraphemeDaoFactory(dbProvider);
  }

  public static GraphemeDao provideGraphemeDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGraphemeDao(db));
  }
}
