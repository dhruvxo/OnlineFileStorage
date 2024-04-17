from pytube import YouTube 
import sys




def download(id,path):
        link="https://www.youtube.com/watch?v={}".format(id.strip())
        try: 
            # object creation using YouTube 
            yt = YouTube(link) 
        except: 
            #to handle exception 
            print(yt)
            print("Connection Error") 

        mp4_streams = yt.streams.filter(file_extension='mp4').all()

        d_video = mp4_streams[-1]

        try: 
            # downloading the video 
            d_video.download(output_path=path)
            print('OK')
        except: 
            print("ERROR")


if __name__=="__main__":
    id=sys.argv[1]
    path=sys.argv[2]
    download(id,path)