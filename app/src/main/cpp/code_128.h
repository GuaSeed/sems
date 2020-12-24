#ifndef CODE_128_H
#define CODE_128_H

#include <iostream>
#include <opencv2/opencv.hpp>

class code_128{
public:
    struct barcode_info {
        // 开始的位置
        int start_pos;
        // 结束的位置
        int stop_pos;
        // 平均单位
        int min_unit;
    };
    static std::string recognition(cv::Mat &src, cv::Rect &rect);

private:
    static ean_13::barcode_info get_min_unit(const uchar *line, int width);

    static std::string decode(const std::vector<float> &barcode_data);

    static std::vector<float> convert(const uchar *line, ean_13::barcode_info info);

    static int global_euclidean_distance(const float *a, bool is_l_code, float &weight);

    static float local_euclidean_distance(int index, const float *a, bool is_l_codeL);
};

#endif