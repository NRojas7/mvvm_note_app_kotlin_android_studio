package com.bersyte.noteapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bersyte.noteapp.MainActivity
import com.bersyte.noteapp.model.Note
import com.bersyte.noteapp.toast
import com.bersyte.noteapp.viewmodel.NoteViewModel
import com.bersyte.noteapp.R
import com.bersyte.noteapp.databinding.FragmentUpdateNoteBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    val REQUEST_IMAGE_GET = 1
    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.etNoteBodyUpdate.setText(currentNote.noteBody)
        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)

        if (currentNote.imageUri != null) {
            val parsedUri = Uri.parse(currentNote.imageUri)
            binding.ivNoteImage.setImageURI(parsedUri)
        }

        binding.fabDone.setOnClickListener {
            val title = binding.etNoteTitleUpdate.text.toString().trim()
            val body = binding.etNoteBodyUpdate.text.toString().trim()

            if (title.isNotEmpty()) {
                val note = Note(currentNote.id, title, body, currentNote.imageUri)
                noteViewModel.updateNote(note)

                view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)

            } else {
                activity?.toast("Enter a note title please")
            }
        }
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
                currentNote.imageUri = imageUriCopy.absolutePath
                // display image
                binding.ivNoteImage.setImageURI(selectedImageUri)
            }
        }
    }

    private fun File.copyInputStreamtoFile(inputStream : InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                noteViewModel.deleteNote(currentNote)
                view?.findNavController()?.navigate(
                    R.id.action_updateNoteFragment_to_homeFragment
                )
            }
            setNegativeButton("CANCEL", null)
        }.create().show()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()
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