# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Room
-keep class androidx.room.** { *; }

# Keep Gson
-keep class com.google.gson.** { *; }

# Keep PrimCalendar
-keep class com.aminography.primcalendar.** { *; }

# Keep application classes
-keep class ir.inspectionmanager.** { *; }

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
