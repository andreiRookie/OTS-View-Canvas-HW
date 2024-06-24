package otus.homework.customview.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import otus.homework.customview.R
import otus.homework.customview.di.AppComponent

class CategoryDetailsFragment : Fragment(R.layout.fragment_category_details_layout) {

    private lateinit var viewModel: CategoryDetailsViewModel

    private lateinit var categoryTitle: TextView
//    private lateinit var categoryDetailsView: CategoryDetailsView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = AppComponent.INSTANCE.categoryDetailsViewModel
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryTitle = view.findViewById(R.id.category_title)
//        categoryDetailsView = view.findViewById(R.id.graph)

        parentFragmentManager.setFragmentResultListener(ARG_RESULT_KEY, this) { _, bundle ->
            val result = bundle.getParcelable(RESULT_VALUE, String::class.java)
            result?.let { viewModel.updateStateWithCategory(it) }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            categoryTitle.text = state.categoryName
//            categoryDetailsView.setData(state.categoryDetailsGraphModel)
        }
    }

    companion object {
        const val ARG_RESULT_KEY = "result key"
        const val RESULT_VALUE = "result"
    }
}