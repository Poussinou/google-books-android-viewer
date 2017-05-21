package com.ames.books;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.ames.books.presenter.ShowDetailsListener;
import com.google.api.services.books.model.Volume;

/**
 * The main activity of the application
 */
public class BookListActivity extends Activity implements ShowDetailsListener {
  private static final String TAG = "books.Booklist";
  private boolean onList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book_list);
    applyState(savedInstanceState);
  }

  private void applyState(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      // hot start
      boolean list = savedInstanceState.getBoolean("a.list", true);
      Log.d(TAG, "list "+list);
      if (list) {
        showList();
      } else {
        showDetails(null, null);
      }
    } else {
      showList(); // cold start
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean("a.list", onList);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    applyState(savedInstanceState);
  }

  public void showList() {
    final FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.show(fragmentManager.findFragmentById(R.id.book_list));
    ft.hide(fragmentManager.findFragmentById(R.id.book_details));
    ft.commit();
    onList = true;
  }

  /**
   * Hide list, show details and instruct the details view to show the selected book.
   */
  @Override
  public void showDetails(Volume book, Drawable thumb) {
    final FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction ft = fragmentManager.beginTransaction();
    BookDetailsFragment details = (BookDetailsFragment) fragmentManager.findFragmentById(R.id.book_details);

    if (book != null) {
      // If null passed, we only configure fragment transaction here.
      details.showDetails(book, thumb);
    }

    ft.show(details);
    ft.hide(fragmentManager.findFragmentById(R.id.book_list));
    ft.addToBackStack("details"); // Use the back button to return to the search list view.

    ft.commit();
    onList = false;
  }
}