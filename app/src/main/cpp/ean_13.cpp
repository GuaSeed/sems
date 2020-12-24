#include "ean_13.h"

std::string ean_13::recognition(cv::Mat &src, cv::Rect &rect) {
    cv::Mat temp(src, rect);
    cv::cvtColor(temp, temp, cv::COLOR_BGR2GRAY);
    // 中值滤波，消除椒盐噪声
    cv::medianBlur(temp, temp, 3);
    cv::threshold(temp, temp, 128, 255, cv::THRESH_BINARY);
//    cv::rectangle(src, rect, cv::Scalar(0, 0, 255), 1);
//    imshow("src", src);
//    cv::waitKey();
    // 取1/3到2/3之间的条形码
    for (int i = temp.cols / 3; i < temp.cols / 3 * 2; ++i) {
        auto line = temp.ptr<uchar>(i);
//        print_line(line, temp.cols);
        // 找到最小值
        ean_13::barcode_info info = get_min_unit(line, temp.cols);
//        print_line(line, info);
        // 转换为vector
        std::vector<float> barcode_data = convert(line, info);
        // 解码
        std::string ret = decode(barcode_data);
        if (!ret.empty()) {
//            print_line(line, temp.cols);
//            LOGD("\ninfo: start_pos: %d, stop_pos: %d, min_unit: %d, %d\n",
//                            info.start_pos, info.stop_pos, info.min_unit, rect.area());
//            print_line(line, info);
//            print_vector(barcode_data, info);
            return ret;
        }
    }
    return std::string();
}

std::string ean_13::decode(const std::vector<float> &data) {
    if (data.size() < EAN_13_LENGTH) {
        return std::string();
    }
    // 去除掉头
    int start = EAN_13_START_TAG_LENGTH;
    // 去除尾
    int stop = EAN_13_LENGTH - EAN_13_START_TAG_LENGTH;
    std::vector<ean_13::barcode_ret> ret;
    for (int x = 0; x < 10; ++x) {
        std::string barcode;
        bool is_l_code = true;
        int check_sum = 0;
        int count = 0;
        int check_digit = 0;
        // 权值
        float weight_sum = 0;
        for (int i = start; i < stop;) {
            if (i >= 27 && i < 27 + 5) {
                i++;
                continue;
            }
            if (i < 27) {
                is_l_code = EAN_13_STRUCTURE_LEFT[x][count] == 1;
            } else {
                is_l_code = true;
            }
            float a[EAN_13_CHAR_BLOCK_LENGTH];
            for (int j = 0; j < EAN_13_CHAR_BLOCK_LENGTH; ++j) {
                a[j] = data[i++];
            }
            int code = global_euclidean_distance(a, is_l_code, weight_sum);
            check_digit = code;
            barcode += std::to_string(code);
            if (count < 11) {
                check_sum += EAN_13_CHECK_DIGIT[count] * code;
            }
            count++;
        }
        check_sum += x;
        int current_check_digit = 10 - (check_sum % 10);
        if (current_check_digit == 10) {
            current_check_digit = 0;
        }
//        std::cout << check_digit << "," << check_sum << "," << current_check_digit << ","
//                  << weight_sum << "," << ret << std::endl;
        if (check_digit == current_check_digit) {
            ret.push_back({
                                  .weight_sum = weight_sum,
                                  .barcode = std::to_string(x) + barcode
                          });
        }
    }
    if (!ret.empty()) {
        float weight_sum = ret[0].weight_sum;
        std::string barcode = ret[0].barcode;
        for (auto r : ret) {
//            LOGD("\nbarcode: %s\n", r.barcode.data());
            if (r.weight_sum < weight_sum) {
                barcode = r.barcode;
            }
        }
        return barcode;
    }
    return std::string();
}

std::vector<float> ean_13::convert(const uchar *line, ean_13::barcode_info info) {
    std::vector<float> ret;
    for (int i = info.start_pos; i < info.stop_pos; i++) {
        int startValue = line[i];
        float continueUnit = 1;
        while (i != info.stop_pos - 1 && line[i + 1] == startValue) {
            continueUnit++;
            i++;
        }
        float count = continueUnit / (float) info.min_unit;
        ret.push_back(count);
    }
    return ret;
}

ean_13::barcode_info ean_13::get_min_unit(const uchar *line, int width) {
    ean_13::barcode_info info{};
    // 是否找到头
    bool is_find_head = false;
    // 当前黑白条总数块
    int block_sum = 0;
    int char_sum = 0;
    // 前一个黑白条的值和前前一个的黑白条的值
    int prev = -1, prev_prev = -1;
    int prev_char_sum = 0, prev_prev_char_sum = 0;
    for (int i = 0; i < width;) {
        int startValue = line[i];
        int continueUnit = 1;
        while (i != width - 1 && line[i + 1] == startValue) {
            continueUnit++;
            i++;
        }
        // 找到开头
        if (!is_find_head && prev_prev == 0 && prev == 255 && startValue == 0) {
            info.start_pos = i - prev_char_sum - prev_prev_char_sum - continueUnit + 1;
            char_sum += continueUnit + prev_prev_char_sum + prev_char_sum;
            is_find_head = true;
            block_sum = 3;
        } else if (is_find_head) {
            char_sum += continueUnit;
            block_sum++;
            // 找到结尾
            if (block_sum >= EAN_13_LENGTH && prev_prev == 0 && prev == 255 && startValue == 0) {
                info.stop_pos = i + 1;
                break;
            }
        }
        prev_prev = prev;
        prev_prev_char_sum = prev_char_sum;
        prev = startValue;
        prev_char_sum = continueUnit;
        info.stop_pos = i + 1;
        i++;
    }
    info.min_unit = (int) round((float) char_sum / EAN_13_BLOCK_LENGTH);
    if (info.min_unit == 0) {
        info.min_unit = 1;
    }
    return info;
}

float ean_13::local_euclidean_distance(int index, const float *a, bool is_l_code) {
    float sum = 0;
    for (int i = 0; i < 3; ++i) {
        float temp = 0;
        for (int j = i; j <= i + 1; ++j) {
            if (is_l_code) {
                temp += a[j] - (float) L_CODE_PATTERN_LENGTH[index][j];
            } else {
                temp += a[j] - (float) G_CODE_PATTERN_LENGTH[index][j];
            }
        }
        sum += pow(temp, 2);
    }
    return sqrt(sum);
}

int ean_13::global_euclidean_distance(const float *a, bool is_l_code, float &weight) {
    float ret[CODE_PATTERN_LENGTH];
    for (int z = 0; z < CODE_PATTERN_LENGTH; ++z) {
        float sum = 0;
        for (int i = 0; i < EAN_13_CHAR_BLOCK_LENGTH; ++i) {
            if (is_l_code) {
                sum += pow(a[i] - (float) L_CODE_PATTERN_LENGTH[z][i], 2);
            } else {
                sum += pow(a[i] - (float) G_CODE_PATTERN_LENGTH[z][i], 2);
            }
        }
        ret[z] = sqrt(sum) + 0.5 * local_euclidean_distance(z, a, is_l_code);
    }
    float min = ret[0];
    int index = 0;
    for (int i = 1; i < CODE_PATTERN_LENGTH; i++) {
        if (ret[i] < min) {
            index = i;
            min = ret[i];
        }
    }
    weight += min;
    return index;
}

void print_vector(const std::vector<float> &barcode_data, ean_13::barcode_info info) {
//    std::cout << "--------------------------" << std::endl;
    std::string log_str;
    LOGD("\n------------%lu-------------\n", barcode_data.size());
    int newLine = 1;
    log_str += "\n";
    bool is_head = true;
    int num = 0;
    for (auto data : barcode_data) {
        num++;
//        std::cout << data << ",";
        log_str += std::to_string(data) + ",";
        if (is_head && newLine == EAN_13_START_TAG_LENGTH) {
            newLine = 1;
            is_head = false;
//            std::cout << std::endl;
            log_str += "\n";
            continue;
        }
        if (num >= 27 && num <= 32) {
//            std::cout << std::endl;
            log_str += "\n";
            newLine = 0;
        } else {
            if (newLine == EAN_13_CHAR_BLOCK_LENGTH) {
                newLine = 0;
//                std::cout << std::endl;
//                LOGD("\n");
                log_str += "\n";
            }
        }
        newLine++;
    }
//    std::cout << std::endl << "--------------------------" << std::endl;
//    LOGD("\n--------------------------\n");
    LOGD("%s", log_str.data());
}

void print_line(const uchar *line, ean_13::barcode_info info) {
    std::string log_str;
    LOGD("\n----------%d---------------\n", info.stop_pos - info.start_pos);
    int newLine = 1;
    log_str += "\n";
    for (int i = info.start_pos; i < info.stop_pos; ++i) {
        log_str += std::to_string((int) (line[i] == 255 ? 0 : 1));
        if (newLine == EAN_13_CHAR_LENGTH * info.min_unit) {
            newLine = 0;
            log_str += "\n";
        }
        newLine++;
    }
    LOGD("%s", log_str.data());
}

void print_line(const uchar *line, int width) {
    print_line(line, {
            .start_pos = 0,
            .stop_pos = width,
            .min_unit = 1
    });
}