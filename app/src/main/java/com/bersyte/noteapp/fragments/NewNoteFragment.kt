package com.bersyte.noteapp.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bersyte.noteapp.MainActivity
import com.bersyte.noteapp.R
import com.bersyte.noteapp.databinding.FragmentNewNoteBinding
import com.bersyte.noteapp.model.Note
import com.bersyte.noteapp.toast
import com.bersyte.noteapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream


class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    val REQUEST_IMAGE_GET = 1
    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var newNote: Note
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewNoteBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        mView = view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            // get the image data from the gallery intent
            val selectedImageUri: Uri? = data?.data
            // read the image uri using content resolver
            val resolver = requireContext().contentResolver
            // create a new file for the uri to be copied to
            val imageUriCopy = File(requireContext().filesDir, "test.jpg")

            if (selectedImageUri != null) {
                // open stream to write data to the imageUriCopy file
                val outputStream = FileOutputStream(imageUriCopy)
                // read the image uri data and copy it to the imageUriCopy file
                resolver.openInputStream(selectedImageUri)?.copyTo(outputStream)
                // save the file path to the db
                //note.imageUri = imageUriCopy.absolutePath
                // display image
                binding.ivNoteImage.setImageURI(selectedImageUri)
            }
        }
    }

    private fun saveNote(view: View) {
        val imageUriCopy = File(requireContext().filesDir,"test.jpg")
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()
        val noteUri = imageUriCopy.absolutePath
        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteBody, noteUri)

            noteViewModel.addNote(note)
            Snackbar.make(
                view, "Note saved successfully",
                Snackbar.LENGTH_SHORT
            ).show()
            view.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)

        } else {
            activity?.toast("Please enter note title")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_new_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                saveNote(mView)
            }

            R.id.menu_addPhoto -> {
                // open the gallery and retrive a photo uri
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    type = "image/*"
                }
                startActivityForResult(intent, REQUEST_IMAGE_GET)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}