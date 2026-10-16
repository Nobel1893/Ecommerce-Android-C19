package com.route.firstapp.ui.home.categoriesSection

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.firstapp.R
import com.route.firstapp.ui.home.HomeSectionTitle
import kotlinx.coroutines.Dispatchers


@Composable
fun HomeCategoriesSection(modifier: Modifier = Modifier,
                          viewModel: HomeCategoriesContract.ViewModel = hiltViewModel<HomeCategoriesViewModel>()
                          ) {
    Dispatchers.Default
    val context = LocalContext.current

    val states = viewModel.states.collectAsStateWithLifecycle()

    val events = viewModel.events.collectAsState(null)

    when(val event = events.value){
        is HomeCategoriesContract.CategoriesEvent.ShowToast ->{
            Toast.makeText(
                context,
                event.message,
                Toast.LENGTH_LONG
            ).show()
        }
        null -> {}
    }

    HomeSectionTitle(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.cateogries)
    )



    when(val state = states.value){

        is HomeCategoriesContract.CategoriesState.CategoriesSuccessState -> {
            HomeCategoriesSuccess(
                state.categories
            )
        }

        is HomeCategoriesContract.CategoriesState.CategoriesErrorState -> {
            HomeCategoriesErrorState(
                onRetryClick = {
                    viewModel.doIntent(
                        HomeCategoriesContract.Intent.LoadCategories
                    )
                }
            )
        }
        is HomeCategoriesContract.CategoriesState.CategoriesLoadingState -> {
            HomeCategoriesLoading()
        }
    }

}
