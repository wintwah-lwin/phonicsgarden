package com.example.phonicsgarden.data.repository;

import com.example.phonicsgarden.data.local.dao.GraphemeDao;
import com.example.phonicsgarden.data.local.dao.WordDao;
import com.example.phonicsgarden.data.remote.DatamuseApi;
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
public final class WordRepositoryImpl_Factory implements Factory<WordRepositoryImpl> {
  private final Provider<GraphemeDao> graphemeDaoProvider;

  private final Provider<WordDao> wordDaoProvider;

  private final Provider<DatamuseApi> apiProvider;

  public WordRepositoryImpl_Factory(Provider<GraphemeDao> graphemeDaoProvider,
      Provider<WordDao> wordDaoProvider, Provider<DatamuseApi> apiProvider) {
    this.graphemeDaoProvider = graphemeDaoProvider;
    this.wordDaoProvider = wordDaoProvider;
    this.apiProvider = apiProvider;
  }

  @Override
  public WordRepositoryImpl get() {
    return newInstance(graphemeDaoProvider.get(), wordDaoProvider.get(), apiProvider.get());
  }

  public static WordRepositoryImpl_Factory create(Provider<GraphemeDao> graphemeDaoProvider,
      Provider<WordDao> wordDaoProvider, Provider<DatamuseApi> apiProvider) {
    return new WordRepositoryImpl_Factory(graphemeDaoProvider, wordDaoProvider, apiProvider);
  }

  public static WordRepositoryImpl newInstance(GraphemeDao graphemeDao, WordDao wordDao,
      DatamuseApi api) {
    return new WordRepositoryImpl(graphemeDao, wordDao, api);
  }
}
