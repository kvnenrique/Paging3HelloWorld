package com.aethink.paging3helloworld.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.aethink.paging3helloworld.R
import com.aethink.paging3helloworld.model.New
import com.aethink.paging3helloworld.viewmodel.MainViewModel

@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {

    // LazyPagingItems<T> handles updated and diffing of the paged list. It automatically requests
    // new pages as the user scrolls.
    /**
     * The collectAsLazyPagingItems() function is designed to work in tandem with the remember
     * function. its actual implementation is something like this:
     *
     *      @Composable
     *      fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(): LazyPagingItems<T> {
     *          // This is the key part!
     *          val lazyPagingItems = remember {
     *              LazyPagingItems(...) // Actual initialization logic
     *          }
     *
     *          // The implementation then handles observing the flow and updating the state
     *          // of the lazyPagingItems object.
     *          // This part runs on every recomposition to check for new emissions.
     *
     *          return lazyPagingItems
     *      }
     *
     * So, while the LazyPagingItems itself is immutable, when its internal state changes it
     * triggers recomposition.
     */
    val news = mainViewModel.newsFlow.collectAsLazyPagingItems()


    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
        ) {
            //
            // Background
            //
            Box(modifier = Modifier.fillMaxSize().background(Color(240, 220, 220)))


            //
            //  News feed
            //
            when {
                // Initial load
                news.loadState.refresh is LoadState.Loading && news.itemCount == 0 -> {
                    LoadingScreen()
                }

                // No data received, even when all went fine
                news.loadState.refresh is LoadState.NotLoading && news.itemCount == 0 -> {
                    EmptyDataScreen()
                }

                // An error occurred
                news.loadState.hasError -> {
                    LoadErrorScreen()
                }

                else -> {
                    NewFeedView(news)

                    // If there are items being append to the end of the list, show the progress
                    // indicator on top the PersonListView.
                    if (news.loadState.append is LoadState.Loading) {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}




//
// Inner components
//
@Composable
fun NewFeedView(news: LazyPagingItems<New>) {
    LazyColumn(

        // The vertical arrangement of the layout's children, allows to add a spacing between items
        verticalArrangement = Arrangement.spacedBy(25.dp)

    ) {
        items(news.itemCount) {
            news[it]?.let { new ->
                NewView(new)
            }
        }
    }
}

@Composable
fun NewView(new: New) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width=1.5.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(15)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(15)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = new.content,
                color = Color.Black,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Preview
@Composable
fun NewViewPreview() {
    NewView(New("The Era of A.I. Propaganda Has Arrived, and America Must Act"))
}

@Preview
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp),
            color = Color.Black
        )
    }
}

@Preview
@Composable
fun EmptyDataScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .widthIn(max = 150.dp),
                text = "It's too empty here",
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun LoadErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                modifier = Modifier
                    .height(60.dp)
                    .aspectRatio(1.0f),
                painter = painterResource(id = R.drawable.no_signal_icon),
                contentDescription = "No signal"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .widthIn(max = 150.dp),
                text = "Something went wrong",
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}