package com.example.foodie.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodie.BuildConfig
import com.example.foodie.R
import com.example.foodie.databinding.FragmentGptBinding
import com.example.foodie.util.Constants.Companion.OPENAI_KEY
import com.example.foodie.util.Message
import com.example.foodie.util.User
import com.example.foodie.viewmodels.GptViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.Locale

class GptFragment : Fragment() {

    private var _binding: FragmentGptBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GptViewModel

//    lateinit var messagesList: MessagesList
//    lateinit var us: User
//    lateinit var chatGpt: User
    lateinit var adapter: MessagesListAdapter<Message>
    lateinit var tts: TextToSpeech
    lateinit var speechRecognizer: SpeechRecognizer
    private val imageLoader: ImageLoader = object : ImageLoader {
        override fun loadImage(imageView: ImageView?, url: String?, payload: Any?) {
            imageView?.load(url) {
                placeholder(R.drawable.loading)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGptBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(GptViewModel::class.java)

        setupAdapter()
        observeViewModel()



//        var imageLoader: ImageLoader = object : ImageLoader {
//            override fun loadImage(imageView: ImageView?, url: String?, payload: Any?) {
//                imageView?.load(url) {
//                    placeholder(R.drawable.loading)
//                }
//            }
//        }

        tts = TextToSpeech(requireContext(), TextToSpeech.OnInitListener {
            if (it != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.UK)
            }
        })

//        messagesList = binding.messagesList
//        adapter =
//            MessagesListAdapter<Message>("1", imageLoader)
//        messagesList.setAdapter(adapter)
//
//        us = User("1", "Tim", "")
//        chatGpt = User("2", "ChatGPT", "")
//
//        binding.sendBtn.setOnClickListener {
//            if (binding.editText.text.trim().isEmpty()) {
//                return@setOnClickListener
//            }
//            var message: Message =
//                Message("m1", binding.editText.text.toString(), us, Calendar.getInstance().time, "")
//            adapter.addToStart(message, true)
//            if (binding.editText.text.toString().startsWith("generate image")) {
//                val placeholderMessage = Message(
//                    "m_temp",
//                    "image",
//                    chatGpt,
//                    Calendar.getInstance().time,
//                    "https://static.vecteezy.com/system/resources/thumbnails/011/299/215/small/simple-loading-or-buffering-icon-design-png.png"
//                )
//                adapter.addToStart(placeholderMessage, true)
//                openAiGenerateImage(binding.editText.text.toString(), placeholderMessage)
//            } else {
//                openAiGenerateText(binding.editText.text.toString())
//            }
//            hideKeyboard()
//            binding.editText.text.clear()
//            binding.lottieLoadingAnimation.visibility = View.VISIBLE
//            binding.lottieLoadingAnimation.playAnimation()
//            binding.textViewWait.visibility = View.VISIBLE
//            binding.scrollView.fullScroll(View.FOCUS_DOWN)
//        }

        binding.sendBtn.setOnClickListener {
            val inputText = binding.editText.text.toString()
            viewModel.handleInput(inputText)
            handleUI()
        }


        binding.sendBtn.setOnLongClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), 121)
            } else {
                var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                speechRecognizer.startListening(intent)
                hideKeyboard()
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            }
            true
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())

        var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
                binding.lottieRecordingAnimation.visibility = View.VISIBLE
                binding.lottieRecordingAnimation.playAnimation()
            }

            override fun onRmsChanged(p0: Float) {
            }

            override fun onBufferReceived(p0: ByteArray?) {
            }

            override fun onEndOfSpeech() {
                binding.lottieRecordingAnimation.visibility = View.GONE
                binding.lottieRecordingAnimation.pauseAnimation()
            }

            override fun onError(p0: Int) {
            }

            override fun onResults(results: Bundle?) {
                var arrayOfRes = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                var message: Message =
                    Message("m1", arrayOfRes!!.get(0), viewModel.us, Calendar.getInstance().time, "")
                adapter.addToStart(message, true)
                if (arrayOfRes!!.get(0).startsWith("generate image")) {
                    val placeholderMessage = Message(
                        "m_temp",
                        "image",
                        viewModel.chatGpt,
                        Calendar.getInstance().time,
                        "https://static.vecteezy.com/system/resources/thumbnails/011/299/215/small/simple-loading-or-buffering-icon-design-png.png"
                    )
                    adapter.addToStart(placeholderMessage, true)
                    openAiGenerateImage(arrayOfRes!!.get(0), placeholderMessage)
                } else {
                    openAiGenerateText(arrayOfRes!!.get(0))
                }
                binding.lottieLoadingAnimation.visibility = View.VISIBLE
                binding.lottieLoadingAnimation.playAnimation()
                binding.textViewWait.visibility = View.VISIBLE
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            }

            override fun onPartialResults(p0: Bundle?) {
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
            }

        })

        return binding.root
    }

    private fun setupAdapter() {
        adapter = MessagesListAdapter<Message>("1", imageLoader)
        binding.messagesList.setAdapter(adapter)
    }

    private fun observeViewModel() {
        viewModel.uiActions.observe(viewLifecycleOwner) { action ->
            when (action) {
                is GptViewModel.UiAction.AddMessage -> adapter.addToStart(action.message, true)
                is GptViewModel.UiAction.GenerateImage -> openAiGenerateImage(action.input, action.placeholderMessage)
                is GptViewModel.UiAction.GenerateText -> openAiGenerateText(action.input)
            }
        }
    }

    private fun handleUI() {
        hideKeyboard()
        binding.editText.text.clear()
        binding.lottieLoadingAnimation.visibility = View.VISIBLE
        binding.lottieLoadingAnimation.playAnimation()
        binding.textViewWait.visibility = View.VISIBLE
        binding.scrollView.fullScroll(View.FOCUS_DOWN)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chefgpt_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    var enableTTS: Boolean = false
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.voice) {
            if (enableTTS) {
                enableTTS = false
                item.setIcon(R.drawable.baseline_voice_over_off_24)
                tts.stop()
            } else {
                enableTTS = true
                item.setIcon(R.drawable.baseline_record_voice_over_24)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openAiGenerateText(input: String) {
// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(requireContext())
        val url = "https://api.openai.com/v1/chat/completions"

        val jsonObject = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("temperature", 0.7)
            put("max_tokens", 50)

            val message = JSONObject().apply {
                put("role", "user")
                put("content", input)
            }

            val messagesArray = JSONArray().put(message)
            put("messages", messagesArray)
        }

// Request a string response from the provided URL.
        val stringRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener<JSONObject> { response ->
                // Display the first 500 characters of the response string.
                var answer =
                    response.getJSONArray("choices").getJSONObject(0).getJSONObject("message")
                        .getString("content")
//                binding.result.text = answer
                var message: Message =
                    Message("m2", answer.trim(), viewModel.chatGpt, Calendar.getInstance().time, "")
                adapter.addToStart(message, true)

                if (enableTTS) {
                    tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null, null)
                }

                binding.lottieLoadingAnimation.visibility = View.GONE
                binding.lottieLoadingAnimation.pauseAnimation()
                binding.textViewWait.visibility = View.GONE
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            },
            Response.ErrorListener {
                binding.lottieLoadingAnimation.visibility = View.GONE
                binding.lottieLoadingAnimation.pauseAnimation()
                binding.textViewWait.visibility = View.GONE
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
                context?.let {
                Toast.makeText(
                    requireContext(),
                    "Network error, please try again",
                    Toast.LENGTH_SHORT
                ).show()
                }}) {

            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["Content-Type"] = "application/json"
                map["Authorization"] =
                    OPENAI_KEY
                return map
            }
        }
        stringRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 60000
            }

            override fun getCurrentRetryCount(): Int {
                return 15
            }

            override fun retry(error: VolleyError?) {

            }

        })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun openAiGenerateImage(input: String, placeholderMessage: Message) {
// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(requireContext())
        val url = "https://api.openai.com/v1/images/generations"

        val jsonObject = JSONObject().apply {
            put("prompt", input)
            put("n", 1)
            put("size", "512x512")
//256x256, 512x512, or 1024x1024.
        }

// Request a string response from the provided URL.
        val stringRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener<JSONObject> { response ->

                // Display the first 500 characters of the response string.
                var answer = response.getJSONArray("data").getJSONObject(0).getString("url")
                var message: Message =
                    Message(
                        placeholderMessage.messageId,
                        "image",
                        viewModel.chatGpt,
                        Calendar.getInstance().time,
                        answer
                    )
                adapter.update(placeholderMessage.messageId, message)

                binding.lottieLoadingAnimation.visibility = View.GONE
                binding.lottieLoadingAnimation.pauseAnimation()
                binding.textViewWait.visibility = View.GONE
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            },
            Response.ErrorListener {
                adapter.delete(placeholderMessage)
                binding.lottieLoadingAnimation.visibility = View.GONE
                binding.lottieLoadingAnimation.pauseAnimation()
                binding.textViewWait.visibility = View.GONE
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
                Toast.makeText(
                    requireContext(),
                    "Network error, please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["Content-Type"] = "application/json"
                map["Authorization"] =
                    OPENAI_KEY
                return map
            }
        }
        stringRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 60000
            }

            override fun getCurrentRetryCount(): Int {
                return 15
            }

            override fun retry(error: VolleyError?) {

            }

        })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = view?.findFocus()
        if (currentFocusView != null) {
            imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isRemoving || requireActivity().isFinishing) {
            // Clear the ViewModel
            viewModelStore.clear()
        }
//        _binding = null
    }
}