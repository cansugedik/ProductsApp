package com.example.productsapp;

import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseIndexArray;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import static com.firebase.ui.common.Preconditions.assertNonNull;
import static com.firebase.ui.common.Preconditions.assertNull;

import android.annotation.SuppressLint;

public class FirebaseRecyclerOptions <T, M extends RecyclerView.ViewHolder> {

    private static final String ERR_SNAPSHOTS_SET = "Snapshot array already set. " +
            "Call only one of setSnapshotArray, setQuery, or setIndexedQuery.";
    private static final String ERR_SNAPSHOTS_NULL = "Snapshot array cannot be null. " +
            "Call one of setSnapshotArray, setQuery, or setIndexedQuery.";

    private final ObservableSnapshotArray<T> mSnapshots;
    private final LifecycleOwner mOwner;


    public FirebaseRecyclerOptions(ObservableSnapshotArray<T> mSnapshots, LifecycleOwner mOwner) {

        this.mSnapshots = mSnapshots;
        this.mOwner = mOwner;
    }

    /**
     * Get the {@link ObservableSnapshotArray} to listen to.
     */
    @NonNull
    public ObservableSnapshotArray<T> getSnapshots() {
        return mSnapshots;
    }

    /**
     * Get the (optional) LifecycleOwner. Listening will start/stop after the appropriate lifecycle
     * events.
     */
    @Nullable
    public LifecycleOwner getOwner() {
        return mOwner;
    }

    /**
     * Builder for a {@link FirebaseRecyclerOptions}.
     *
     * @param <T> the model class for the {@link// FirebaseRecyclerAdapter}.
     */
    public static final class Builder<T> {

        private ObservableSnapshotArray<T> mSnapshots;
        private LifecycleOwner mOwner;

        /**
         * Directly set the {@link ObservableSnapshotArray} to be listened to.
         * <p>
         * Do not call this method after calling {@code setQuery}.
         */
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder<T> setSnapshotArray(@NonNull ObservableSnapshotArray<T> snapshots) {
            assertNull(mSnapshots, ERR_SNAPSHOTS_SET);

            mSnapshots = snapshots;
            return this;
        }

        /**
         * Set the Firebase query to listen to, along with a {@link SnapshotParser} to parse
         * snapshots into model objects.
         * <p>
         * Do not call this method after calling {@link #setSnapshotArray(ObservableSnapshotArray)}.
         */
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder<T> setQuery(@NonNull Query query,
                                   @NonNull SnapshotParser<T> snapshotParser) {
            assertNull(mSnapshots, ERR_SNAPSHOTS_SET);

            mSnapshots = new FirebaseArray<>(query, snapshotParser);
            return this;
        }

        /**
         * Set the Firebase query to listen to, along with a {@link Class} to which snapshots should
         * be parsed.
         * <p>
         * Do not call this method after calling {@link #setSnapshotArray(ObservableSnapshotArray)}.
         */
        @NonNull
        public Builder<T> setQuery(@NonNull Query query) {
            return setQuery(query);
        }


        /**
         * Set an indexed Firebase query to listen to, along with a {@link SnapshotParser} to parse
         * snapshots into model objects. Keys are identified by the {@code keyQuery} and then data
         * is fetched using those keys from the {@code dataRef}.
         * <p>
         * Do not call this method after calling {@link #setSnapshotArray(ObservableSnapshotArray)}.
         */
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder<T> setIndexedQuery(@NonNull Query keyQuery,
                                          @NonNull DatabaseReference dataRef,
                                          @NonNull SnapshotParser<T> snapshotParser) {
            assertNull(mSnapshots, ERR_SNAPSHOTS_SET);

            mSnapshots = new FirebaseIndexArray<>(keyQuery, dataRef, snapshotParser);
            return this;
        }

        /**
         * Set an indexed Firebase query to listen to, along with a {@link Class} to which snapshots
         * should be parsed. Keys are identified by the {@code keyQuery} and then data is fetched
         * using those keys from the {@code dataRef}.
         * <p>
         * Do not call this method after calling {@link #setSnapshotArray(ObservableSnapshotArray)}.
         */
        @NonNull
        public Builder<T> setIndexedQuery(@NonNull Query keyQuery,
                                          @NonNull DatabaseReference dataRef,
                                          @NonNull Class<T> modelClass) {
            return setIndexedQuery(keyQuery, dataRef, new ClassSnapshotParser<>(modelClass));
        }

        /**
         * Set the (optional) {@link LifecycleOwner}. Listens will start and stop after the
         * appropriate lifecycle events.
         */
        @NonNull
        public Builder<T> setLifecycleOwner(@Nullable LifecycleOwner owner) {
            mOwner = owner;
            return this;
        }

        /**
         * Build a {@link FirebaseRecyclerOptions} from the provided arguments.
         */
        @SuppressLint("RestrictedApi")
        @NonNull
        public FirebaseRecyclerOptions<T, RecyclerView.ViewHolder> build() {
            assertNonNull(mSnapshots, ERR_SNAPSHOTS_NULL);

            return new FirebaseRecyclerOptions<>(mSnapshots, mOwner);
        }
    }

}
