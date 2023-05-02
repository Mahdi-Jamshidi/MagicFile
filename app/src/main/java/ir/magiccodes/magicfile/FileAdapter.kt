package ir.magiccodes.magicfile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.net.URLConnection

class FileAdapter(private val data: ArrayList<File>, private val fileEvent: FileEvent) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
    private var ourViewType = 0

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt = itemView.findViewById<TextView>(R.id.textView)
        val img = itemView.findViewById<ImageView>(R.id.imageView)

        fun bindViews(file: File) {

            // memeType => http://androidxref.com/4.4.4_r1/xref/frameworks/base/media/java/android/media/MediaFile.java#174
            var fileType = ""

            txt.text = file.name

            if (file.isDirectory) {
                img.setImageResource(R.drawable.ic_folder)
            } else {

                when {

                    isImage(file.path) -> {
                        img.setImageResource(R.drawable.ic_image)
                        fileType = "image/*"
                    }

                    isVideo(file.path) -> {
                        img.setImageResource(R.drawable.ic_video)
                        fileType = "video/*"
                    }

                    isZip(file.name) -> {
                        img.setImageResource(R.drawable.ic_zip)
                        fileType = "application/zip"
                    }

                    else -> {
                        img.setImageResource(R.drawable.ic_file)
                        fileType = "text/plain"
                    }

                }

            }

            itemView.setOnClickListener {

                if (file.isDirectory) {
                    fileEvent.onFolderClicked(file.path)
                } else {
                    fileEvent.onFileClicked(file, fileType)
                }

            }

            itemView.setOnLongClickListener {
                fileEvent.onLongClicked(file, adapterPosition)
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {

        val view: View

        if (viewType == 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_linear, parent, false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_grid, parent, false)
        }

        return FileViewHolder(view)
    }
    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bindViews(data[position])
    }
    override fun getItemCount(): Int {
        return data.size
    }
    override fun getItemViewType(position: Int): Int {
        return ourViewType
    }

    private fun isImage(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }
    private fun isVideo(path: String): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("video")
    }
    private fun isZip(name: String): Boolean {
        return name.contains(".zip") || name.contains(".rar")
    }

    fun addNewFile(newFile: File) {

        data.add(0, newFile)
        notifyItemInserted(0)

    }
    fun removeFile(oldFile: File, position: Int) {
        data.remove(oldFile)
        notifyItemRemoved(position)
    }
    fun changeViewType(newViewType: Int) {
        ourViewType = newViewType
        notifyDataSetChanged()
    }

    interface FileEvent {

        fun onFileClicked(file: File, type: String)
        fun onFolderClicked(path: String)
        fun onLongClicked(file: File, position: Int)

    }
}