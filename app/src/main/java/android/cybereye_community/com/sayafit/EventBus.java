package android.cybereye_community.com.sayafit;

import com.google.gson.Gson;

import org.jetbrains.annotations.Contract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import timber.log.Timber;


/**
 * Created by Mutya Nayavashti on 02/01/2017.
 */

public class EventBus <T>{

  /*  private static class SingletonHolder{
        public static EventBus singletonInstance =
                new EventBus();
    }
    // SingletonExample prevents any other class from instantiating
    private  EventBus() {
    }

    // Providing Global point of access
    @Contract(pure = true)
    public static EventBus getInstance() {
        return SingletonHolder.singletonInstance;
    }
*/
  private static EventBus mInstance;

    public static EventBus getInstance() {
        if (mInstance == null) {
            mInstance = new EventBus();
        }
        return mInstance;
    }


    private Subject<T,T> mSubject = PublishSubject.create();

    /**
     * Pass a String down to event listeners.
     */
    public void setObservable(T object) {
        mSubject.onNext(object);
    }

    /**
     * Subscribe to this Observable. On event, do something e.g. replace a fragment
     */
    public Observable getObservable() {
        return mSubject;
    }
}
