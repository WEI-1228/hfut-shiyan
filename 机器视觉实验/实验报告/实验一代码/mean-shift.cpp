#include<opencv2/opencv.hpp>
#include<math.h>
#include<map>
using namespace cv;
using namespace std;

//C:\\Users\\WEI\\Desktop\\��������\\BSDS300\\images\\train\\
//L:\\software\\OpenCV\\opencv\\sources\\samples\\data\\

String image_path = "L:\\software\\OpenCV\\opencv\\sources\\samples\\data\\desert.jpg";	//����ͼ��λ��
int hr = 8; //����߶�
int hs = 32;  //��ɫ�߶�
int iter = 100; //����������
int blur_size = 3;  //ģ������˴�С

//GaussianBlur��medianBlur
String blur_mode = "GaussianBlur";	//ģ������ѡ��


int ColorThreshold = 10; //ֹͣ����ʱ ��С�仯���ز�ֵ
int merge_mode = 0;			//0�Ǵ�һ���㿪ʼ��ɢ���ο��㲻�䣻1�ǲο���������ɢ���ƶ�
int mergeThreshold = 60; //�ϲ����ز�ֵ

bool merge_small_block = 0;	//�Ƿ�ϲ�С����
int lowThreshold = 30;    //����ֵ����lowThreshold��Ҫ���ϲ�
int hiThreshold = 100;    //���ϲ���������Ҫ�ϲ�������hiThreshold�ĵط�

int imageRows, imageCols; //����ͼ�񳤿���Ϣ
int x = 0, y = 0;
int calPixel(Vec3b pixelPre,Vec3b pixelNow) {	//�������ص�pixelPre��pixelNow֮��ľ���ƽ��
	return pow((pixelNow[0] - pixelPre[0]), 2) + pow((pixelNow[1] - pixelPre[1]), 2) + pow((pixelNow[2] - pixelPre[2]), 2);
}

bool checkColor(Mat& src, int x, int y, int x1, int y1) {
	Vec3b pixelPre = src.at<Vec3b>(x, y);
	Vec3b pixelNow = src.at<Vec3b>(x1, y1);
	return (calPixel(pixelNow, pixelPre) < hs);
}

bool checkLen(Mat& src, int x, int y, int x1, int y1) {
	return (x - x1) * (x - x1) + (y - y1) * (y - y1) <= hr;
}

void findMean(Mat& src, int row, int col,int step) {
	Vec3b pixelPre = src.at<Vec3b>(row, col);
	int xsum = 0;
	int ysum = 0;
	int count = 0;
	for (int i = row - hr; i <= row + hr && i < src.rows; i++) {
		if (i < 0)i = 0;
		for (int j = col - hr; j <= col + hr && j < src.cols; j++) {
			if (j < 0)j = 0;
			if (checkLen(src, i, j, row, col) && checkColor(src, i, j, row, col)) {
				xsum += i;
				ysum += j;
				++count;
			}
		}
	}

	xsum = xsum / count;
	ysum = ysum / count;

	Vec3b pixelNow = src.at<Vec3b>(xsum, ysum);
	if (calPixel(pixelPre, pixelNow) < ColorThreshold || step > iter) {
		x = xsum;
		y = ysum;
		return;
	}
	else findMean(src, xsum, ysum, step + 1);
}


vector<Point>vec;
int b, g, r;



bool check(int rows,int cols,int x,int y) {
	return x >= 0 && x < rows&& y >= 0 && y < cols;
}

Vec3b curPixel;

//��ջ
void dfs(vector<vector<int>>& marked, int x, int y, Mat& bgr_src) {
	int rows = bgr_src.rows, cols = bgr_src.cols;
	vec.push_back(Point(x, y));
	marked[x][y] = 1;
	b += bgr_src.at<Vec3b>(x, y)[0];
	g += bgr_src.at<Vec3b>(x, y)[1];
	r += bgr_src.at<Vec3b>(x, y)[2];
	if (check(rows, cols, x - 1, y) && !marked[x - 1][y] && calPixel(curPixel, bgr_src.at<Vec3b>(x - 1, y)) <= mergeThreshold)dfs(marked, x - 1, y, bgr_src);
	if (check(rows, cols, x + 1, y) && !marked[x + 1][y] && calPixel(curPixel, bgr_src.at<Vec3b>(x + 1, y)) <= mergeThreshold)dfs(marked, x + 1, y, bgr_src);
	if (check(rows, cols, x, y + 1) && !marked[x][y + 1] && calPixel(curPixel, bgr_src.at<Vec3b>(x, y + 1)) <= mergeThreshold)dfs(marked, x, y + 1, bgr_src);
	if (check(rows, cols, x, y - 1) && !marked[x][y - 1] && calPixel(curPixel, bgr_src.at<Vec3b>(x, y - 1)) <= mergeThreshold)dfs(marked, x, y - 1, bgr_src);
}

RNG rng((unsigned)time(NULL));

int id = 1;
vector<vector<int>> partID;  //ÿ�����ض�Ӧ�������
map<int, int>part_count;  //ÿ������Ŷ�Ӧ�����ظ���

void bfs(vector<vector<int>>& marked,Mat& partImg, int x, int y, Mat& bgr_src) {
	int rows = bgr_src.rows, cols = bgr_src.cols;
	queue<Point>queue;
	marked[x][y] = 1;
	queue.push(Point(x, y));
	
	Vec3b color;
	color[0] = rng.uniform(0, 255);
	color[1] = rng.uniform(0, 255);
	color[2] = rng.uniform(0, 255);
	int countPart = 0;
	while (!queue.empty()) {
		Point point = queue.front();
		queue.pop();
		x = point.x;
		y = point.y;
		if (merge_mode)curPixel = bgr_src.at<Vec3b>(x, y);
		partID[x][y] = id;
		++countPart;
		b += bgr_src.at<Vec3b>(x, y)[0];
		g += bgr_src.at<Vec3b>(x, y)[1];
		r += bgr_src.at<Vec3b>(x, y)[2];
		partImg.at<Vec3b>(x, y) = color;
		vec.push_back(Point(x, y));
		int t;
		if (x - 1 >= 0) {
			t = calPixel(curPixel, bgr_src.at<Vec3b>(x - 1, y));
		}
		if (check(rows, cols, x - 1, y) && !marked[x - 1][y] && calPixel(curPixel, bgr_src.at<Vec3b>(x - 1, y)) <= mergeThreshold) {
			marked[x - 1][y] = 1;
			queue.push(Point(x - 1, y));
		}
		if (x + 1 < imageRows) {
			t = calPixel(curPixel, bgr_src.at<Vec3b>(x + 1, y));
		}
		if (check(rows, cols, x + 1, y) && !marked[x + 1][y] && calPixel(curPixel, bgr_src.at<Vec3b>(x + 1, y)) <= mergeThreshold) {
			marked[x + 1][y] = 1;
			queue.push(Point(x + 1, y));
		}
		if (y + 1 < imageCols) {
			t = calPixel(curPixel, bgr_src.at<Vec3b>(x, y + 1));
		}
		if (check(rows, cols, x, y + 1) && !marked[x][y + 1] && calPixel(curPixel, bgr_src.at<Vec3b>(x, y + 1)) <= mergeThreshold) {
			marked[x][y + 1] = 1;
			queue.push(Point(x, y + 1));
		}
		if (y - 1 >= 0) {
			t = calPixel(curPixel, bgr_src.at<Vec3b>(x, y - 1));
		}
		if (check(rows, cols, x, y - 1) && !marked[x][y - 1] && calPixel(curPixel, bgr_src.at<Vec3b>(x, y - 1)) <= mergeThreshold) {
			marked[x][y - 1] = 1;
			queue.push(Point(x, y - 1));
		}

	}
	part_count.insert(pair<int, int>(id, countPart));
}


void mergeImg(Mat& src,Mat&dst) {
	b = b / vec.size();
	g = g / vec.size();
	r = r / vec.size();
	for (Point p : vec) {
		dst.at<Vec3b>(p.x, p.y)[0] = b;
		dst.at<Vec3b>(p.x, p.y)[1] = g;
		dst.at<Vec3b>(p.x, p.y)[2] = r;
	}
}

void search(Mat& src,Mat& colorImg, int x, int y) {
	vector<vector<int>> visited(imageRows, vector<int>(imageCols));
	vector<Point>list;
	queue<Point>queue;
	queue.push(Point(x, y));
	visited[x][y] = 1;
	Point targetPoint;
	int targetID;
	while (!queue.empty()) {
		Point point = queue.front();
		queue.pop();
		int x = point.x;
		int y = point.y;
		int tmpID = partID[x][y];
		int tmpCount = part_count[tmpID];
		if (tmpCount > hiThreshold) {
			targetPoint = point;
			targetID = tmpID;
			break;
		}
		if (tmpCount < lowThreshold)list.push_back(Point(x, y));
		if (check(imageRows, imageCols, x - 1, y) && !visited[x - 1][y]) {
			visited[x - 1][y] = 1;
			queue.push(Point(x - 1, y));
		}
		if (check(imageRows, imageCols, x + 1, y) && !visited[x + 1][y]) {
			visited[x + 1][y] = 1;
			queue.push(Point(x + 1, y));
		}
		if (check(imageRows, imageCols, x, y - 1) && !visited[x][y - 1]) {
			visited[x][y - 1] = 1;
			queue.push(Point(x, y - 1));
		}
		if (check(imageRows, imageCols, x, y + 1) && !visited[x][y + 1]) {
			visited[x][y + 1] = 1;
			queue.push(Point(x, y + 1));
		}
	}
	Vec3b color = src.at<Vec3b>(targetPoint.x, targetPoint.y);
	Vec3b color2 = colorImg.at<Vec3b>(targetPoint.x, targetPoint.y);
	for (Point p : list) {
		src.at<Vec3b>(p.x, p.y) = color;
		colorImg.at<Vec3b>(p.x, p.y) = color2;
		partID[p.x][p.y] = targetID;
	}
	part_count[targetID] = part_count[targetID] + list.size();
}


void mergeSmall(Mat &src,Mat &colorImg) {
	for (int row = 0; row < imageRows; row++) {
		for (int col = 0; col < imageCols; col++) {
			int curID = partID[row][col];	//��ǰ�����ID���
			int partCount = part_count[curID];	//�ҵ���ǰ��������ظ���
			if (partCount > lowThreshold)continue;	//������ظ���������ֵ���򲻽��кϲ�
			search(src,colorImg, row, col);		//����ͨ����������С����ϲ�
		}
	}
}





int main(int argc, char** argv) {
	mergeThreshold = mergeThreshold * mergeThreshold;
	hs = hs * hs;
	ColorThreshold = ColorThreshold * ColorThreshold;

	Mat src = imread(image_path);
	//imshow("input",src);
	
	Mat blur_src = Mat::zeros(src.size(), src.type());
	//

	if (blur_mode == "medianBlur") {
		medianBlur(src, blur_src, blur_size);
	}
	else {
		GaussianBlur(src, blur_src, Size(blur_size, blur_size), 9);
	}

	Mat partImage = Mat::zeros(src.size(), src.type());


	
	Mat bgr_dst = Mat::zeros(src.size(), src.type());     //��ɫ���
	int rows = src.rows,cols = src.cols;
	imageRows = rows, imageCols = cols;
	for (int row = 0; row < rows; row++) {
		for (int col = 0; col < cols; col++) {
			findMean(blur_src, row, col, 1);
			bgr_dst.at<Vec3b>(row, col) = blur_src.at<Vec3b>(x, y);
		}
	}

	vector<vector<int>> marked (rows, vector<int>(cols));
	partID = vector<vector<int>>(imageRows, vector<int>(imageCols));
	

	Mat dst = Mat::zeros(bgr_dst.size(), bgr_dst.type());
	for (int row = imageRows - 1; row >= 0; row--) {
		for (int col = imageCols - 1; col >= 0; col--) {
			//�����ǰ���ص�δ�����ʹ�����ô����bfs���ϲ���Χ����
			if (!marked[row][col]) {
				//��vecװӦ���ϲ������ص�
				vec.clear();
				//���кϲ����ص�r,g,b�ĺ�
				b = 0, g = 0, r = 0;
				//��ǰ��ʼ�����ϲ������ص�
				curPixel = bgr_dst.at<Vec3b>(row, col);
				//bfsѰ�����������Ƶ����ص�
				bfs(marked, partImage, row, col, bgr_dst);
				//��Ѱ�ҵ����������ص�ƽ����rgb������ͼ��dst
				mergeImg(bgr_dst, dst);
				id++;
			}
		}
	}

	imshow("raw_image", src);
	imshow("blur_image", blur_src);
	//imshow("gray image", gray);
	//imshow("gray_res img", gray_dst);
	imshow("bgr_res img", bgr_dst);
	imshow("final_res", dst);
	imshow("part_img", partImage);
	if (merge_small_block) {
		Mat merge = dst.clone();
		Mat merge_part_img = partImage.clone();
		mergeSmall(merge, merge_part_img);
		imshow("merge_res", merge);
		imshow("merge_part_img", merge_part_img);
	}
	waitKey(0);
}