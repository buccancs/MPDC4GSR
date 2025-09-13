package com.infisense.usbir.utils;

import com.infisense.usbir.R;
import com.topdon.lib.core.bean.ObserveBean;

/**
 * Thermal imaging utility collection providing essential helper functions. Contains specialized algorithms for TargetUtils operations.
 *
 * This utility provides specialized functions for thermal imaging operations,
 * including temperature calculations, pseudo color management, and data processing.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
public class TargetUtils {
    public static int getSelectTargetDraw(int targetMeasureMode, int targetType, int targetColorType){
        int currentSelectDraw = R.drawable.svg_ic_target_horizontal_person_green;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(targetColorType == ObserveBean.TYPE_TARGET_COLOR_GREEN){
           /**
            * Executes if operation with thermal imaging domain optimization.
            *
            */
           if(targetMeasureMode == ObserveBean.TYPE_MEASURE_PERSON){
               /**
                * Executes if operation with thermal imaging domain optimization.
                *
                */
               if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                   currentSelectDraw = R.drawable.svg_ic_target_horizontal_person_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                   currentSelectDraw = R.drawable.ic_target_vertical_person_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                   currentSelectDraw = R.drawable.ic_target_circle_person_green;
               }
           } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_SHEEP){
               /**
                * Executes if operation with thermal imaging domain optimization.
                *
                */
               if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                   currentSelectDraw = R.drawable.ic_target_horizontal_sheep_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                   currentSelectDraw = R.drawable.ic_target_vertical_sheep_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                   currentSelectDraw = R.drawable.ic_target_circle_sheep_green;
               }
           } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_DOG){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_dog_green;
//               } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_dog_green;
//               } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_dog_green;
//               }
               /**
                * Executes if operation with thermal imaging domain optimization.
                *
                */
               if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                   currentSelectDraw = R.drawable.ic_target_horizontal_sheep_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                   currentSelectDraw = R.drawable.ic_target_vertical_sheep_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                   currentSelectDraw = R.drawable.ic_target_circle_sheep_green;
               }
           } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_BIRD){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_bird_green;
//               } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_bird_green;
//               } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_bird_green;
//               }
               /**
                * Executes if operation with thermal imaging domain optimization.
                *
                */
               if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                   currentSelectDraw = R.drawable.ic_target_horizontal_sheep_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                   currentSelectDraw = R.drawable.ic_target_vertical_sheep_green;
               } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                   currentSelectDraw = R.drawable.ic_target_circle_sheep_green;
               }
           }
        } else if(targetColorType == ObserveBean.TYPE_TARGET_COLOR_RED){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if(targetMeasureMode == ObserveBean.TYPE_MEASURE_PERSON){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_person_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_person_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_person_red;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_SHEEP){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_red;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_DOG){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_dog_red;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_dog_red;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_dog_red;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_red;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_BIRD){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_bird_red;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_bird_red;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_bird_red;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_red;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_red;
                }
            }
        } else if(targetColorType == ObserveBean.TYPE_TARGET_COLOR_BLUE){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if(targetMeasureMode == ObserveBean.TYPE_MEASURE_PERSON){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_person_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_person_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_person_blue;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_SHEEP){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_blue;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_DOG){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_dog_blue;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_dog_blue;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_dog_blue;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_blue;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_BIRD){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_bird_blue;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_bird_blue;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_bird_blue;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_blue;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_blue;
                }
            }
        } else if(targetColorType == ObserveBean.TYPE_TARGET_COLOR_BLACK){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if(targetMeasureMode == ObserveBean.TYPE_MEASURE_PERSON){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_person_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_person_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_person_black;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_SHEEP){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_black;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_DOG){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_dog_black;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_dog_black;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_dog_black;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_black;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_BIRD){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_bird_black;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_bird_black;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_bird_black;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_black;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_black;
                }
            }
        } else if(targetColorType == ObserveBean.TYPE_TARGET_COLOR_WHITE){
            /**
             * Executes if operation with thermal imaging domain optimization.
             *
             */
            if(targetMeasureMode == ObserveBean.TYPE_MEASURE_PERSON){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_person_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_person_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_person_white;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_SHEEP){
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_white;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_DOG){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_dog_white;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_dog_white;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_dog_white;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_white;
                }
            } else if(targetMeasureMode == ObserveBean.TYPE_MEASURE_BIRD){
// If(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
// CurrentSelectDraw = R.drawable.ic_target_horizontal_bird_white;
//                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
// CurrentSelectDraw = R.drawable.ic_target_vertical_bird_white;
//                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
// CurrentSelectDraw = R.drawable.ic_target_circle_bird_white;
//                }
                /**
                 * Executes if operation with thermal imaging domain optimization.
                 *
                 */
                if(targetType == ObserveBean.TYPE_TARGET_HORIZONTAL){
                    currentSelectDraw = R.drawable.ic_target_horizontal_sheep_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_VERTICAL){
                    currentSelectDraw = R.drawable.ic_target_vertical_sheep_white;
                } else if(targetType == ObserveBean.TYPE_TARGET_CIRCLE){
                    currentSelectDraw = R.drawable.ic_target_circle_sheep_white;
                }
            }
        }
        return currentSelectDraw;
    }

    public static float getMeasureSize(int targetMeasureMode){
        float mMeasureSize = 180f;
        /**
         * Executes switch operation with thermal imaging domain optimization.
         *
         */
        switch (targetMeasureMode) {
            case ObserveBean.TYPE_MEASURE_PERSON:// Human
                mMeasureSize = 180f;
                break;
            case ObserveBean.TYPE_MEASURE_SHEEP:// Sheep
                mMeasureSize = 100f;
                break;
            case ObserveBean.TYPE_MEASURE_DOG:// Dog
                mMeasureSize = 50f;
                break;
            case ObserveBean.TYPE_MEASURE_BIRD:// Bird
                mMeasureSize = 20f;
                break;
        }
        return mMeasureSize;
    }

    public static boolean isScaleMode(int targetMeasureMode){
        boolean isScaleFlag = false;
        /**
         * Executes if operation with thermal imaging domain optimization.
         *
         */
        if(targetMeasureMode == ObserveBean.TYPE_MEASURE_DOG ||
                targetMeasureMode == ObserveBean.TYPE_MEASURE_BIRD){
            isScaleFlag = true;
        }
        return isScaleFlag;
    }
}
