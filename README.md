# GUI_Imager


This tool was a tool built for a specific Initial application process Test case.
Test case requirements:
•	Video required of all steps of Initial application process on a consumer facing app.
•	Screenshots of each changed screen required
•	Redaction required on all sensitive details (credit card address etc).
The user would launch a batch script grabbing frames at 1 second interval of original unredacted video.
GUI_Imger would launch prepopulated with all these image files.

User could quickly scan over images for sensitive info. 
using point and click and point toggle user could get required coordinates for a blur redaction filter that was inputted into ffmpeg.

Ffmpeg would be used to split original video redact section required and stitch back up.

Original screenshots were submitted as required.
In conjunction with external ffmpeg scripts videos could be loaded in as single images. The tool could then be used to quickly redact, crop or generate "point" info for use on a blur filter on an original video file.

While this tool as very little practical use outside of this test it allowed the user to produce much faster results given:

-Expected Turnaround time was in minutes not hours mean time of output dropped from 45 mins to ~17
-Rectangle blur was all that was required from ffmpeg script
-Original video could be pieced together redacting sensitive information for customer
-using default settings original video would be packaged in expected .mp4 format for customer.
-using java and ffmpeg mean this could be easily deployed on MacOS Linux and windows based machines

