package com.example.phonicsgarden;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.example.phonicsgarden.data.local.AppDatabase;
import com.example.phonicsgarden.data.local.dao.AttemptDao;
import com.example.phonicsgarden.data.local.dao.GraphemeDao;
import com.example.phonicsgarden.data.local.dao.MasteryDao;
import com.example.phonicsgarden.data.local.dao.WordDao;
import com.example.phonicsgarden.data.remote.DatamuseApi;
import com.example.phonicsgarden.data.repository.ProgressRepositoryImpl;
import com.example.phonicsgarden.data.repository.SettingsRepository;
import com.example.phonicsgarden.data.repository.WordRepositoryImpl;
import com.example.phonicsgarden.di.DatabaseModule_ProvideAttemptDaoFactory;
import com.example.phonicsgarden.di.DatabaseModule_ProvideDatabaseFactory;
import com.example.phonicsgarden.di.DatabaseModule_ProvideGraphemeDaoFactory;
import com.example.phonicsgarden.di.DatabaseModule_ProvideMasteryDaoFactory;
import com.example.phonicsgarden.di.DatabaseModule_ProvideWordDaoFactory;
import com.example.phonicsgarden.di.NetworkModule_ProvideDatamuseApiFactory;
import com.example.phonicsgarden.di.NetworkModule_ProvideJsonFactory;
import com.example.phonicsgarden.di.NetworkModule_ProvideRetrofitFactory;
import com.example.phonicsgarden.engine.ChallengeGenerator;
import com.example.phonicsgarden.engine.WordChallengeGenerator;
import com.example.phonicsgarden.ui.games.bubble.BubblePopViewModel;
import com.example.phonicsgarden.ui.games.bubble.BubblePopViewModel_HiltModules;
import com.example.phonicsgarden.ui.games.monster.FeedMonsterViewModel;
import com.example.phonicsgarden.ui.games.monster.FeedMonsterViewModel_HiltModules;
import com.example.phonicsgarden.ui.games.tracing.TracingViewModel;
import com.example.phonicsgarden.ui.games.tracing.TracingViewModel_HiltModules;
import com.example.phonicsgarden.ui.landing.LandingViewModel;
import com.example.phonicsgarden.ui.landing.LandingViewModel_HiltModules;
import com.example.phonicsgarden.ui.settings.SettingsViewModel;
import com.example.phonicsgarden.ui.settings.SettingsViewModel_HiltModules;
import com.example.phonicsgarden.ui.stats.StatsViewModel;
import com.example.phonicsgarden.ui.stats.StatsViewModel_HiltModules;
import com.example.phonicsgarden.util.TtsSpeaker;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;
import retrofit2.Retrofit;

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
public final class DaggerPhonicsApp_HiltComponents_SingletonC {
  private DaggerPhonicsApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public PhonicsApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements PhonicsApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public PhonicsApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements PhonicsApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public PhonicsApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements PhonicsApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public PhonicsApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements PhonicsApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public PhonicsApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements PhonicsApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public PhonicsApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements PhonicsApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public PhonicsApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements PhonicsApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public PhonicsApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends PhonicsApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends PhonicsApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends PhonicsApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends PhonicsApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(6).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_games_bubble_BubblePopViewModel, BubblePopViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_games_monster_FeedMonsterViewModel, FeedMonsterViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_landing_LandingViewModel, LandingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_stats_StatsViewModel, StatsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_games_tracing_TracingViewModel, TracingViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_phonicsgarden_ui_games_monster_FeedMonsterViewModel = "com.example.phonicsgarden.ui.games.monster.FeedMonsterViewModel";

      static String com_example_phonicsgarden_ui_settings_SettingsViewModel = "com.example.phonicsgarden.ui.settings.SettingsViewModel";

      static String com_example_phonicsgarden_ui_games_bubble_BubblePopViewModel = "com.example.phonicsgarden.ui.games.bubble.BubblePopViewModel";

      static String com_example_phonicsgarden_ui_landing_LandingViewModel = "com.example.phonicsgarden.ui.landing.LandingViewModel";

      static String com_example_phonicsgarden_ui_stats_StatsViewModel = "com.example.phonicsgarden.ui.stats.StatsViewModel";

      static String com_example_phonicsgarden_ui_games_tracing_TracingViewModel = "com.example.phonicsgarden.ui.games.tracing.TracingViewModel";

      @KeepFieldType
      FeedMonsterViewModel com_example_phonicsgarden_ui_games_monster_FeedMonsterViewModel2;

      @KeepFieldType
      SettingsViewModel com_example_phonicsgarden_ui_settings_SettingsViewModel2;

      @KeepFieldType
      BubblePopViewModel com_example_phonicsgarden_ui_games_bubble_BubblePopViewModel2;

      @KeepFieldType
      LandingViewModel com_example_phonicsgarden_ui_landing_LandingViewModel2;

      @KeepFieldType
      StatsViewModel com_example_phonicsgarden_ui_stats_StatsViewModel2;

      @KeepFieldType
      TracingViewModel com_example_phonicsgarden_ui_games_tracing_TracingViewModel2;
    }
  }

  private static final class ViewModelCImpl extends PhonicsApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<BubblePopViewModel> bubblePopViewModelProvider;

    private Provider<FeedMonsterViewModel> feedMonsterViewModelProvider;

    private Provider<LandingViewModel> landingViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StatsViewModel> statsViewModelProvider;

    private Provider<TracingViewModel> tracingViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.bubblePopViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.feedMonsterViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.landingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.statsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.tracingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_games_bubble_BubblePopViewModel, ((Provider) bubblePopViewModelProvider)).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_games_monster_FeedMonsterViewModel, ((Provider) feedMonsterViewModelProvider)).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_landing_LandingViewModel, ((Provider) landingViewModelProvider)).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_stats_StatsViewModel, ((Provider) statsViewModelProvider)).put(LazyClassKeyProvider.com_example_phonicsgarden_ui_games_tracing_TracingViewModel, ((Provider) tracingViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_phonicsgarden_ui_games_monster_FeedMonsterViewModel = "com.example.phonicsgarden.ui.games.monster.FeedMonsterViewModel";

      static String com_example_phonicsgarden_ui_games_bubble_BubblePopViewModel = "com.example.phonicsgarden.ui.games.bubble.BubblePopViewModel";

      static String com_example_phonicsgarden_ui_landing_LandingViewModel = "com.example.phonicsgarden.ui.landing.LandingViewModel";

      static String com_example_phonicsgarden_ui_games_tracing_TracingViewModel = "com.example.phonicsgarden.ui.games.tracing.TracingViewModel";

      static String com_example_phonicsgarden_ui_settings_SettingsViewModel = "com.example.phonicsgarden.ui.settings.SettingsViewModel";

      static String com_example_phonicsgarden_ui_stats_StatsViewModel = "com.example.phonicsgarden.ui.stats.StatsViewModel";

      @KeepFieldType
      FeedMonsterViewModel com_example_phonicsgarden_ui_games_monster_FeedMonsterViewModel2;

      @KeepFieldType
      BubblePopViewModel com_example_phonicsgarden_ui_games_bubble_BubblePopViewModel2;

      @KeepFieldType
      LandingViewModel com_example_phonicsgarden_ui_landing_LandingViewModel2;

      @KeepFieldType
      TracingViewModel com_example_phonicsgarden_ui_games_tracing_TracingViewModel2;

      @KeepFieldType
      SettingsViewModel com_example_phonicsgarden_ui_settings_SettingsViewModel2;

      @KeepFieldType
      StatsViewModel com_example_phonicsgarden_ui_stats_StatsViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.phonicsgarden.ui.games.bubble.BubblePopViewModel 
          return (T) new BubblePopViewModel(singletonCImpl.wordRepositoryImplProvider.get(), singletonCImpl.progressRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryProvider.get(), new ChallengeGenerator(), singletonCImpl.ttsSpeakerProvider.get());

          case 1: // com.example.phonicsgarden.ui.games.monster.FeedMonsterViewModel 
          return (T) new FeedMonsterViewModel(singletonCImpl.wordRepositoryImplProvider.get(), singletonCImpl.progressRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryProvider.get(), new WordChallengeGenerator(), singletonCImpl.ttsSpeakerProvider.get());

          case 2: // com.example.phonicsgarden.ui.landing.LandingViewModel 
          return (T) new LandingViewModel(singletonCImpl.wordRepositoryImplProvider.get(), singletonCImpl.progressRepositoryImplProvider.get());

          case 3: // com.example.phonicsgarden.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.settingsRepositoryProvider.get());

          case 4: // com.example.phonicsgarden.ui.stats.StatsViewModel 
          return (T) new StatsViewModel(singletonCImpl.wordRepositoryImplProvider.get(), singletonCImpl.progressRepositoryImplProvider.get());

          case 5: // com.example.phonicsgarden.ui.games.tracing.TracingViewModel 
          return (T) new TracingViewModel(singletonCImpl.wordRepositoryImplProvider.get(), singletonCImpl.progressRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryProvider.get(), singletonCImpl.ttsSpeakerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends PhonicsApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends PhonicsApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends PhonicsApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<Json> provideJsonProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<DatamuseApi> provideDatamuseApiProvider;

    private Provider<WordRepositoryImpl> wordRepositoryImplProvider;

    private Provider<ProgressRepositoryImpl> progressRepositoryImplProvider;

    private Provider<SettingsRepository> settingsRepositoryProvider;

    private Provider<TtsSpeaker> ttsSpeakerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private GraphemeDao graphemeDao() {
      return DatabaseModule_ProvideGraphemeDaoFactory.provideGraphemeDao(provideDatabaseProvider.get());
    }

    private WordDao wordDao() {
      return DatabaseModule_ProvideWordDaoFactory.provideWordDao(provideDatabaseProvider.get());
    }

    private AttemptDao attemptDao() {
      return DatabaseModule_ProvideAttemptDaoFactory.provideAttemptDao(provideDatabaseProvider.get());
    }

    private MasteryDao masteryDao() {
      return DatabaseModule_ProvideMasteryDaoFactory.provideMasteryDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 1));
      this.provideJsonProvider = DoubleCheck.provider(new SwitchingProvider<Json>(singletonCImpl, 4));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 3));
      this.provideDatamuseApiProvider = DoubleCheck.provider(new SwitchingProvider<DatamuseApi>(singletonCImpl, 2));
      this.wordRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<WordRepositoryImpl>(singletonCImpl, 0));
      this.progressRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ProgressRepositoryImpl>(singletonCImpl, 5));
      this.settingsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepository>(singletonCImpl, 6));
      this.ttsSpeakerProvider = DoubleCheck.provider(new SwitchingProvider<TtsSpeaker>(singletonCImpl, 7));
    }

    @Override
    public void injectPhonicsApp(PhonicsApp phonicsApp) {
      injectPhonicsApp2(phonicsApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private PhonicsApp injectPhonicsApp2(PhonicsApp instance) {
      PhonicsApp_MembersInjector.injectWordRepository(instance, wordRepositoryImplProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.phonicsgarden.data.repository.WordRepositoryImpl 
          return (T) new WordRepositoryImpl(singletonCImpl.graphemeDao(), singletonCImpl.wordDao(), singletonCImpl.provideDatamuseApiProvider.get());

          case 1: // com.example.phonicsgarden.data.local.AppDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.example.phonicsgarden.data.remote.DatamuseApi 
          return (T) NetworkModule_ProvideDatamuseApiFactory.provideDatamuseApi(singletonCImpl.provideRetrofitProvider.get());

          case 3: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideJsonProvider.get());

          case 4: // kotlinx.serialization.json.Json 
          return (T) NetworkModule_ProvideJsonFactory.provideJson();

          case 5: // com.example.phonicsgarden.data.repository.ProgressRepositoryImpl 
          return (T) new ProgressRepositoryImpl(singletonCImpl.attemptDao(), singletonCImpl.masteryDao());

          case 6: // com.example.phonicsgarden.data.repository.SettingsRepository 
          return (T) new SettingsRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.example.phonicsgarden.util.TtsSpeaker 
          return (T) new TtsSpeaker(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
