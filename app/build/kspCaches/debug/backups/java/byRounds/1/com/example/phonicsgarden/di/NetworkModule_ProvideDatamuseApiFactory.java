package com.example.phonicsgarden.di;

import com.example.phonicsgarden.data.remote.DatamuseApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class NetworkModule_ProvideDatamuseApiFactory implements Factory<DatamuseApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideDatamuseApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public DatamuseApi get() {
    return provideDatamuseApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideDatamuseApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideDatamuseApiFactory(retrofitProvider);
  }

  public static DatamuseApi provideDatamuseApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideDatamuseApi(retrofit));
  }
}
