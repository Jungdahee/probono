import io
import copy
import re
from google.cloud import vision

class DetectText(object):

    def __init__(self):
        self.text = []

    def detect(self, path):
        client = vision.ImageAnnotatorClient()

        # 이미지 읽기
        with io.open(path, 'rb') as image_file:
            content = image_file.read()

        image = vision.types.Image(content=content)

        # 이미지에서 텍스트 추출
        response = client.text_detection(image=image)
        texts = response.text_annotations[0]

        self.text.append(texts.description.split("\n"))

        return self.text


#구글에서 받은 리스트에서 하나씩 꺼내 정 or 캡슐 or 필름 or 껌 or 밀리그람 or 밀리그램 or mg 들어있는지 확인
#들어있다면 임의의 리스트에 추가함
#임의의 리스트를 디비와 비교

words = ['정', '캡슐', '필름', '껌', '밀리그람', '밀리그램', 'mg', '시럽', '클러', '타이레놀']
stopwords = ['환자정보', '병원정보', '일분', 'Tel', '형정제', '필름코팅', '복용', '묵용', '정제', '규정', '처방전', '환 자 정 보', '환자성명', '경기', '작용',
             '조제', '비)']

class Preprocessing(object):

    def __init__(self):
        self.temp = []
        self.result = []
        self.filtered = []

    def step1(self, sentences):

        for sentence in sentences[0]:
            for word in words:
                if word in sentence:
                    self.temp.append(sentence)


        '''print("임시 리스트 ------------------------------")
        print(len(self.temp))
        print(self.temp)'''

        return self.temp

    def step2(self, tempList):

        temp = copy.deepcopy(tempList)

        for list in tempList:
            for word in stopwords:
                if list.find(word) >= 0:
                    temp.remove(list)
                    break

        tempResult = set(temp)

        print("tempResult:: ")
        print(tempResult)

        for list in tempResult:
            if list.find('(') >= 0:
                text = list[:list.find('(')]
                self.result.append(text)
            else:
                self.result.append(list)

        print("최종 리스트---------------------------------")
        print(len(self.result))
        print(self.result)

        return self.result

    def step3(self, tempList):

        for list in tempList:
            text = re.sub('[-=+,#/\?:^$.@*\"※~&%ㆍ!』\\‘|\(\)\[\]\<\>`\'…》_]', '', list)
            self.filtered.append(text)

        print("특수 기호 제거-------------------------------")
        print(self.filtered)

if __name__ == "__main__":
    DetectText = DetectText()
    text = DetectText.detect("me11.jpg")

    Preprocessing = Preprocessing()
    temp = Preprocessing.step1(text)
    result = Preprocessing.step2(temp)
    filtered = Preprocessing.step3(result)

