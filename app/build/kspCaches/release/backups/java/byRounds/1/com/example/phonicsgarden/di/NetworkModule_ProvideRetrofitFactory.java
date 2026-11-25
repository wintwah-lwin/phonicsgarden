package com.example.phonicsgarden.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final Provider<Json> jsonProvider;

  public NetworkModule_ProvideRetrofitFactory(Provider<Json> jsonProvider) {
    this.jsonProvider = jsonProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(jsonProvider.get());
  }

  public static NetworkModule_ProvideRetrofitFactory create(Provider<Json> jsonProvider) {
    return new NetworkModule_ProvideRetrofitFactory(jsonProvider);
  }

  public static Retrofit provideRetrofit(Json json) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideRetrofit(json));
  }
}
