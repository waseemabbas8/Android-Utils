package softsolutions.com.tutors.utils

interface OnListItemClickListener <T>{
    fun onItemClick(item: T, pos: Int)
}